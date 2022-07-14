package com.zjl.finalarchitecture.module.sysnav.ui.adapter

import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zjl.base.utils.findNavController
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.NavigationVO

/**
 * @description:
 * @author: zhou
 * @date : 2022/7/14 18:29
 */
class FuckNavigationGroupAdapter(private var fargment: Fragment) :
    BaseQuickAdapter<NavigationVO, BaseViewHolder>(R.layout.item_system_group) {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: NavigationChildAdapter


    override fun convert(holder: BaseViewHolder, item: NavigationVO) {
        holder.setText(R.id.system_classify_title, item.name)
        rv = holder.getView<RecyclerView>(R.id.rv_system_child)
        /**
         * 注：我们尽量不要在convert（onBindViewHolder）中做一些固定性的视图创建工作
         * 这样可以省去一些滑动性能开销
         */
        rv.layoutManager = FlexboxLayoutManager(context).apply {
            // 左对齐
            justifyContent = JustifyContent.FLEX_START
        }

        adapter = NavigationChildAdapter()
        rv.adapter = adapter
        adapter.setList(item.articles)

        adapter.setOnItemClickListener { adapter, view, position ->
            val articleVo = item.articles[position]
            fargment.findNavController().navigate(
                NavMainDirections.actionGlobalToWebFragment(articleVo)
            )
        }
    }

}