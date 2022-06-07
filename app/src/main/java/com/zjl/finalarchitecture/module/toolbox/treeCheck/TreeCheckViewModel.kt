package com.zjl.finalarchitecture.module.toolbox.treeCheck

import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeBuilder
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Xiaoc
 * @since 2022-06-06
 *
 * 树状选择ViewModel
 */
class TreeCheckViewModel : BaseViewModel() {

    companion object {

        private val safDocumentProjection = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_FLAGS
        )
    }

    private val atomicLong = AtomicLong(0)

    private val folderRootUris = listOf<Uri>()

    val nodeManager = InMemoryTreeStateManager<FolderNode>()
    private val treeBuilder = TreeBuilder(nodeManager)

    fun addFolderRoot(folderRootUri: Uri) {
        viewModelScope.launch {

            // 先检查一下是否存在它的父级Uri，如果存在则不加入到其中，并加以提示
            val treeDocumentId = DocumentsContract.getTreeDocumentId(folderRootUri)
            val documentIdPrefix = treeDocumentId.split("/").getOrNull(0) ?: ""
            val find = folderRootUris.find {
                it.toString().startsWith(documentIdPrefix)
            }
            if (find != null) {
                return@launch
            }

            // 构建为Document版Uri
            val treeFile = DocumentFile.fromTreeUri(globalContext, folderRootUri)
            treeFile?.let {
                val folderRootNode = FolderNode.RootFolderNode(
                    atomicLong.addAndGet(1),
                    treeFile.uri.toString(),
                    it.name ?: "Unknown",
                    true,
                    1
                )
                treeBuilder.sequentiallyAddNextNode(folderRootNode, 0)
            }
        }
    }

    fun loadChildrenByParentFolder(parentNode: FolderNode) {
        viewModelScope.launch {
            val parentFolderUri = Uri.parse(parentNode.folderUri)
            val documentFile =
                DocumentFile.fromTreeUri(globalContext, parentFolderUri) ?: return@launch

            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                documentFile.uri,
                DocumentsContract.getDocumentId(documentFile.uri)
            )
            val childFolderNode = mutableListOf<FolderNode.ChildFolderNode>()

            globalContext.contentResolver.query(
                childrenUri,
                safDocumentProjection,
                null,
                null,
                null
            )?.use {

                val mediaIdColumnIndex =
                    it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
                val mediaTitleColumnIndex =
                    it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                val mimeTypeColumnIndex =
                    it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE)

                while (it.moveToNext()) {
                    val mimeType = it.getString(mimeTypeColumnIndex)
                    val isDirectory = mimeType == DocumentsContract.Document.MIME_TYPE_DIR
                    if (!isDirectory) {
                        continue
                    }
                    val documentId: String = it.getString(mediaIdColumnIndex)
                    val documentName: String = it.getString(mediaTitleColumnIndex)
                    val documentUri = DocumentsContract.buildDocumentUriUsingTree(
                        parentFolderUri,
                        documentId
                    )

                    childFolderNode.add(
                        FolderNode.ChildFolderNode(
                            atomicLong.addAndGet(1),
                            documentUri.toString(), documentName,
                            selected = parentNode.selected,
                            folderStatus = parentNode.folderStatus
                        )
                    )
                }
            }

            // 如果目录下没有子目录，我们将其刷新并更改界面
            if(childFolderNode.isEmpty()){
                nodeManager.getNodeInfo(parentNode).id.haveChildren = false
                nodeManager.refresh()
                return@launch
            }

            childFolderNode.forEach {
                treeBuilder.addRelation(parentNode, it)
            }
        }

    }


    fun setStatusParentByChild(childNode: FolderNode, targetSelect: Boolean) {
        // 找到该节点的父节点，如果目标是取消勾选，则将父亲进行取消选择操作
        findAndStatusParentNode(childNode, targetSelect)
        nodeManager.refresh()
    }

    private fun findAndStatusParentNode(node: FolderNode, select: Boolean) {
        val parentFatherNode = nodeManager.getParent(node)
        if (parentFatherNode != null) {
            if (!select) {
                parentFatherNode.selected = select
            }
            val parentChildrenNode = nodeManager.getChildren(parentFatherNode)
            val selectCount = parentChildrenNode.count {
                it.selected
            }
            // 如果一个都没有选中，则将状态置为 1
            if (selectCount <= 0 || selectCount == parentChildrenNode.size) {
                parentFatherNode.folderStatus = 1
                node.folderStatus = 1
            } else {
                parentFatherNode.folderStatus = -1
            }
            if (selectCount == parentChildrenNode.size && select) {
                parentFatherNode.selected = select
            }
        } else {
            return
        }
        findAndStatusParentNode(parentFatherNode, select)
    }


    fun handleSelectedChildren(targetSelect: Boolean, parentNode: FolderNode) {
        viewModelScope.launch {
            findAndStatusChildNode(parentNode, targetSelect)
            nodeManager.refresh()
        }
    }

    private fun findAndStatusChildNode(node: FolderNode, selected: Boolean) {
        // 找到该父节点的所有子节点
        val children = nodeManager.getChildren(node)
        if (children.isNullOrEmpty()) {
            return
        }
        children.forEach {
            it.selected = selected
            findAndStatusChildNode(it, selected)
        }

    }

    override fun refresh() {

    }
}