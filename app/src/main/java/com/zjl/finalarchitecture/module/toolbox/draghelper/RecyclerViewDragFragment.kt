package com.zjl.finalarchitecture.module.toolbox.draghelper

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentRecyclerViewDragBinding
import com.zjl.finalarchitecture.module.toolbox.draghelper.adapter.DragStringItemAdapter
import com.zjl.finalarchitecture.module.toolbox.draghelper.adapter.DragStringItemHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-06-14
 *
 * RecyclerView拖拽Item 最佳实践
 * 该内容支持长按拖拽卡片到指定位置
 */
class RecyclerViewDragFragment: BaseFragment<FragmentRecyclerViewDragBinding, RecyclerViewDragViewModel>() {

    private lateinit var dragStringItemAdapter: DragStringItemAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        dragStringItemAdapter = DragStringItemAdapter()

        // 设置ItemTouch辅助工具，让RecyclerView的Item支持拖拽
        // 默认激活是长按，可以配置
        val itemTouchHelper = ItemTouchHelper(
            DragStringItemHelper(dragStringItemAdapter)
        )

        dragStringItemAdapter.itemTouchHelper = itemTouchHelper
        mBinding.rvDrag.adapter = dragStringItemAdapter

        // ItemTouch辅助工具绑定RecyclerView
        itemTouchHelper.attachToRecyclerView(mBinding.rvDrag)
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.items.collectLatest {
                    dragStringItemAdapter.setList(it)
                }
            }
        }
    }
}