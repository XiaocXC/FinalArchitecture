package com.zjl.finalarchitecture.module.toolbox.treeCheck.helper

import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.zjl.base.globalContext
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.widget.treeview.TreeBuilder
import com.zjl.finalarchitecture.widget.treeview.TreeStateManager
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Xiaoc
 * @since  2023-05-20
 *
 * 文件夹树Tree的动态加载帮助类
 **/
class FolderTreeSelectorHelper(
): TreeSelectorHelper<FolderNode>() {

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

    val atomicLong = AtomicLong(0)

    override fun loadChildren(
        parent: FolderNode,
        builder: TreeBuilder<FolderNode>,
        nodeManager: TreeStateManager<FolderNode>
    ) {

        // 构建父节点的DocumentFile
        val parentFolderUri = Uri.parse(parent.folderUri)
        val documentFile =
            DocumentFile.fromTreeUri(globalContext, parentFolderUri) ?: return

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

                val status = if(parent.selected == NODE_UNCHECKED){
                    NODE_UNCHECKED
                } else {
                    NODE_CHECKED
                }

                // 将子文件夹加入到列表中
                childFolderNode.add(
                    FolderNode.ChildFolderNode(
                        atomicLong.addAndGet(1),
                        documentUri.toString(),
                        documentName,
                        selected = status
                    )
                )
            }
        }

        // 如果目录下没有子目录，我们将其刷新并更改界面
        if(childFolderNode.isEmpty()){
            nodeManager.getNodeInfo(parent).id.haveChildren = false
            nodeManager.refresh()
            return
        }

        // 告知manager将子节点加入到对应父节点的内容下
        childFolderNode.forEach {
            treeBuilder.addRelation(parent, it)
        }
    }

    override fun setNodeStatus(node: FolderNode, status: Int) {
        node.selected = status
    }

    override fun getNodeStatus(node: FolderNode): Int {
        return node.selected
    }
}