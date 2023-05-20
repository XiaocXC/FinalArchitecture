package com.zjl.finalarchitecture.module.toolbox.treeCheck

import android.net.Uri
import android.provider.DocumentsContract
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
            treeFile?.let {
                val folderRootNode = FolderNode.RootFolderNode(
                    folderTreeHelper.atomicLong.addAndGet(1),
                    treeFile.uri.toString(),
                    it.name ?: "Unknown",
                    TreeSelectorHelper.NODE_CHECKED
                )
                // 将根Node节点加入到Tree内容中
                folderTreeHelper.addRoot(folderRootNode, 0)
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