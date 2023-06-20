package com.zjl.finalarchitecture.module.toolbox.treeCheck

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentTreeCheckBinding
import com.zjl.finalarchitecture.module.toolbox.treeCheck.adapter.FolderNodeTreeViewAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * @author Xiaoc
 * @since 2022-06-06
 *
 * 这是一个使用SAF文件选择框架显示一个树状文件夹内容的Fragment
 * 它是一个树状文件夹多选的最佳案例
 */
class TreeCheckFragment: BaseFragment<FragmentTreeCheckBinding, TreeCheckViewModel>() {

    private lateinit var folderNodeTreeViewAdapter: FolderNodeTreeViewAdapter

    /**
     * 启动SAF申请获取对应文件夹权限
     */
    private val actionOpenDocumentTree = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val directoryUri = it.data?.data ?: return@registerForActivityResult

            // 申请保持访问权限Uri
            requireContext().applicationContext.contentResolver.takePersistableUriPermission(
                directoryUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            // 加入到内存中
            mViewModel.addFolderRoot(directoryUri)
        }
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 启动SAF选择可访问路径
        mBinding.btnSelectTree.setOnClickListener {
            actionOpenDocumentTree.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        }

        folderNodeTreeViewAdapter = FolderNodeTreeViewAdapter(
            requireContext(), mViewModel.folderTreeHelper.nodeManager, {
                mViewModel.loadChildrenByParentFolder(it)
            },{ checked, parentNode ->
                mViewModel.applyNodeCheckedChanged(parentNode, checked)
            }
        )

        mBinding.treeViewFolder.adapter = folderNodeTreeViewAdapter
    }

    override fun createObserver() {
        mViewModel.message.launchAndCollectIn(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}