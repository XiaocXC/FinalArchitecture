package com.zjl.finalarchitecture.module.toolbox.treeCheck

import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.module.toolbox.treeCheck.helper.FolderTreeSelectorHelper
import com.zjl.finalarchitecture.module.toolbox.treeCheck.helper.TreeSelectorHelper
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Xiaoc
 * @since 2022-06-06
 *
 * 树状选择ViewModel
 */
class TreeCheckViewModel : BaseViewModel() {

    /**
     * 已经添加的根Uri
     */
    private val folderRootUris = mutableListOf<Uri>()

    private val _message = MutableSharedFlow<Int>()
    val message: SharedFlow<Int> = _message

    val folderTreeHelper = FolderTreeSelectorHelper()

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
            treeFile?.let { documentFile ->

                val folderRootNode = FolderNode.RootFolderNode(
                    folderTreeHelper.atomicLong.addAndGet(1),
                    treeFile.uri.toString(),
                    documentFile.name ?: "Unknown",
                    icon = 0,
                    title = "",
                    TreeSelectorHelper.NODE_CHECKED
                )

                // 将根Node节点加入到Tree内容中
                folderTreeHelper.addRoot(folderRootNode, 0)
                folderRootUris.add(folderRootUri)
                try {
                    Timber.i("TTTTT" + documentFile.uri)
                    globalContext.contentResolver.query(documentFile.uri, null, null, null, null)?.use {
                        while (it.moveToNext()){
                            for(name in it.columnNames){
                                Timber.i(name + " - " + it.getString(it.getColumnIndexOrThrow(name)))
                            }
                            val id = it.getString(it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID))

                            val rootUri = DocumentsContract.buildRootUri(folderRootUri.authority, id)
                            Timber.i("TTTTT" + rootUri)
                            globalContext.contentResolver.query(rootUri, null, null, null, null)?.use {
                                for(name in it.columnNames){
                                    Timber.i(name + " - " + it.getString(it.getColumnIndexOrThrow(name)))
                                }
                            }
                        }
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
//                try {
//                    val rootUri = DocumentsContract.buildRootUri(folderRootUri.authority, DocumentsContract.getTreeDocumentId(folderRootUri))
//                    val client = globalContext.contentResolver.acquireUnstableContentProviderClient(folderRootUri.authority!!)
//                    val cu = client?.query(treeFile.uri, null, null, null, null)
//                    cu?.use {
//
//                        Timber.i("根目录数据: ${it.columnNames.toList().toString()}")
//                        val icon = it.getInt(it.getColumnIndexOrThrow(DocumentsContract.Root.COLUMN_ICON))
//                        val title = it.getString(it.getColumnIndexOrThrow(DocumentsContract.Root.COLUMN_TITLE))
//
//
//                        Timber.i("根目录数据: $folderRootNode")
//                    }
//
//                } catch (e: Exception){
//                    e.printStackTrace()
//                    _message.emit(R.string.description_already_tree)
//                }

            }
        }
    }

    /**
     * 加载对应父节点下的子节点
     * @param parentNode 父Node
     */
    fun loadChildrenByParentFolder(parentNode: FolderNode) {
        viewModelScope.launch {
            folderTreeHelper.loadChildren(parentNode)
        }

    }

    /**
     * 更改节点的选择状态，我们需要更新对应所有父节点和子节点的选择状态
     * @param node 节点
     * @param checked 是否为选中，true：选中 | false：取消
     */
    fun applyNodeCheckedChanged(node: FolderNode, checked: Boolean){
        viewModelScope.launch {
            folderTreeHelper.applyNodeCheckedChanged(node, checked)
        }
    }

    fun initData() {

    }
}