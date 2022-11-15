package com.zjl.finalarchitecture.module.toolbox.treeCheck

import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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

    /**
     * 已经添加的根Uri
     */
    private val folderRootUris = mutableListOf<Uri>()

    private val _message = MutableSharedFlow<Int>()
    val message: SharedFlow<Int> = _message

    /**
     * 节点管理器
     */
    val nodeManager = InMemoryTreeStateManager<FolderNode>()
    private val treeBuilder = TreeBuilder(nodeManager)

    /**
     * 添加根Uri
     * @param folderRootUri 根路径Uri
     */
    fun addFolderRoot(folderRootUri: Uri) {
        viewModelScope.launch {

            // 先检查一下是否存在它的父级Uri，如果存在则不加入到其中，并加以提示
            val treeDocumentId = DocumentsContract.getTreeDocumentId(folderRootUri)
            val documentIdPrefix = treeDocumentId.split("/").getOrNull(0) ?: ""
            val find = folderRootUris.find {
                it.lastPathSegment?.startsWith(documentIdPrefix) ?: false
            }
            if (find != null) {
                _message.emit(R.string.description_already_tree)
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
                // 将根Node节点加入到TreeBuilder中
                treeBuilder.sequentiallyAddNextNode(folderRootNode, 0)
                folderRootUris.add(folderRootUri)
            }
        }
    }

    /**
     * 加载对应父节点下的子节点
     * @param parentNode 父Node
     */
    fun loadChildrenByParentFolder(parentNode: FolderNode) {
        viewModelScope.launch {
            // 构建父节点的DocumentFile
            val parentFolderUri = Uri.parse(parentNode.folderUri)
            val documentFile =
                DocumentFile.fromTreeUri(globalContext, parentFolderUri) ?: return@launch

            /**
             * 根据父节点的DocumentFile构建子节点查询的DocumentFile
             */
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                documentFile.uri,
                DocumentsContract.getDocumentId(documentFile.uri)
            )
            val childFolderNode = mutableListOf<FolderNode.ChildFolderNode>()

            // 查询
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
                    // 判断是否是文件夹类型，如果不是，则不加入到列表中
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

                    // 将子文件夹加入到列表中
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

            // 告知manager将子节点加入到对应父节点的内容下
            childFolderNode.forEach {
                treeBuilder.addRelation(parentNode, it)
            }
        }

    }

    /**
     * 更改了子节点的选择状态，我们需要更新对应所有父节点的选择状态
     * @param childNode 子节点
     * @param targetSelect 是否为选中，true：选中 | false：取消
     */
    fun setStatusParentByChild(childNode: FolderNode, targetSelect: Boolean) {
        // 找到该节点的父节点，如果目标是取消勾选，则将父亲进行取消选择操作
        findAndStatusParentNode(childNode, targetSelect)
        nodeManager.refresh()
    }

    /**
     * 找到并更改对应节点的所有父节点，并更新其状态
     * 这是一个递归方法，直到完全遍历完成
     */
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


    /**
     * 更改了父节点的选择状态，我们需要更新对应所有子节点的选择状态
     * @param parentNode 父节点
     * @param targetSelect 是否为选中，true：选中 | false：取消
     */
    fun handleSelectedChildren(targetSelect: Boolean, parentNode: FolderNode) {
        viewModelScope.launch {
            findAndStatusChildNode(parentNode, targetSelect)
            nodeManager.refresh()
        }
    }

    /**
     * 找到并更改对应节点的所有子节点，并更新其状态
     * 这是一个递归方法，直到完全遍历完成
     */
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

    fun initData() {

    }
}