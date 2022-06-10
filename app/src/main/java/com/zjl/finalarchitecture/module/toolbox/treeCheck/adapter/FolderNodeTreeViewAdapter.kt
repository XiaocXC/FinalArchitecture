package com.zjl.finalarchitecture.module.toolbox.treeCheck.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.widget.treeview.AbstractTreeViewAdapter
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeNodeInfo
import com.zjl.finalarchitecture.widget.treeview.TreeViewList

class FolderNodeTreeViewAdapter(
    context: Context,
    treeStateManager: InMemoryTreeStateManager<FolderNode>,
    treeViewList: TreeViewList,
    private val loadChild: (FolderNode) -> Unit,
    private val selectFolder: (Boolean, FolderNode) -> Unit,
    private val cancelParent: (FolderNode, Boolean) -> Unit
): AbstractTreeViewAdapter<FolderNode>(context, treeViewList, treeStateManager, 5) {

    private val itemClickAction = View.OnClickListener {
        val node = it.tag as FolderNode
        val nodeInfo = manager.getNodeInfo(node)
        // 如果节点有孩子
        if(node.haveChildren){
            when {
                nodeInfo.isWithChildren -> {
                    super.handleItemClick(it, node)
                }
                nodeInfo.level < 4 -> {
                    loadChild(node)
                }
                else -> {
                    // 如果level大于4，则不进行child的加载了，并隐藏展开收缩图标，直接作为选择
                    node.haveChildren = false
                    it.findViewById<ImageView>(R.id.iv_folder_status).visibility = View.INVISIBLE
                }
            }
        } else {
            val targetSelected = !node.selected
            node.selected = targetSelected
            selectFolder(targetSelected, node)
            if(!targetSelected){
                cancelParent(node, targetSelected)
            }
        }
    }

    private val itemCheckedAction =
        CompoundButton.OnCheckedChangeListener { view, isChecked ->
            val node = view.tag as FolderNode

            node.selected = isChecked
            if(node.haveChildren){
                selectFolder(isChecked, node)
            }
            cancelParent(node, isChecked)
        }

    override fun getNewChildView(treeNodeInfo: TreeNodeInfo<FolderNode>): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = if(treeNodeInfo.id is FolderNode.RootFolderNode){
            layoutInflater.inflate(R.layout.item_folder_root, null)
        } else {
            layoutInflater.inflate(R.layout.item_folder_child, null).apply {
                visibility = View.GONE
            }
        }
        viewLayout.setOnClickListener(itemClickAction)
        return updateView(viewLayout, treeNodeInfo)
    }

    override fun updateView(view: View, treeNodeInfo: TreeNodeInfo<FolderNode>): View {
        view.tag = treeNodeInfo.id
        val folderNode = treeNodeInfo.id
        val root = view as ConstraintLayout
        if(folderNode is FolderNode.RootFolderNode){
            val tvFolderName = root.findViewById<TextView>(R.id.tv_folder_name)
            val ivFolderStatus = root.findViewById<ImageView>(R.id.iv_folder_status)
            val cbSelect = root.findViewById<CheckBox>(R.id.cb_select)
            cbSelect.tag = treeNodeInfo.id
            tvFolderName.text = folderNode.folderName
            // 为了避免下面的isChecked影响回调进行多余操作处理，我们会先将事件设置为null，然后再恢复
            cbSelect.setOnCheckedChangeListener(null)
            cbSelect.isChecked = folderNode.selected
            cbSelect.setOnCheckedChangeListener(itemCheckedAction)
            if(folderNode.folderStatus == -1){
                tvFolderName.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
                tvFolderName.paint.isAntiAlias = true//抗锯齿
            } else {
                tvFolderName.paint.flags = 0
            }
            if(folderNode.haveChildren){
                ivFolderStatus.visibility = View.VISIBLE
                if(treeNodeInfo.isExpanded){
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_expand_tip)
                } else {
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_collapse_tip)
                }
            } else {
                ivFolderStatus.visibility = View.INVISIBLE
            }

        } else {
            val tvFolderName = root.findViewById<TextView>(R.id.tv_folder_name)
            val ivFolderStatus = root.findViewById<ImageView>(R.id.iv_folder_status)
            val cbSelect = root.findViewById<CheckBox>(R.id.cb_select)
            cbSelect.tag = treeNodeInfo.id
            tvFolderName.text = folderNode.folderName
            // 为了避免下面的isChecked影响回调进行多余操作处理，我们会先将事件设置为null，然后再恢复
            cbSelect.setOnCheckedChangeListener(null)
            cbSelect.isChecked = folderNode.selected
            cbSelect.setOnCheckedChangeListener(itemCheckedAction)
            if(folderNode.folderStatus == -1){
                tvFolderName.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
                tvFolderName.paint.isAntiAlias = true//抗锯齿
            } else {
                tvFolderName.paint.flags = 0
            }
            if(folderNode.haveChildren){
                ivFolderStatus.visibility = View.VISIBLE
                if(treeNodeInfo.isExpanded){
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_expand_tip)
                } else {
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_collapse_tip)
                }
            } else {
                ivFolderStatus.visibility = View.INVISIBLE
            }
        }
        return view
    }

    override fun getItemId(position: Int): Long {
        return getTreeId(position).id
    }
}