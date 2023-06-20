package com.zjl.finalarchitecture.module.toolbox.treeCheck.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.zjl.base.utils.ext.dp
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.treeCheck.data.FolderNode
import com.zjl.finalarchitecture.module.toolbox.treeCheck.helper.TreeSelectorHelper
import com.zjl.finalarchitecture.widget.treeview.AbstractTreeViewAdapter
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeNodeInfo
import com.zjl.finalarchitecture.widget.treeview.TreeViewList

/**
 * @author Xiaoc
 * @since 2022-06-12
 *
 * 文件夹显示Adapter
 */
class FolderNodeTreeViewAdapter(
    context: Context,
    treeStateManager: InMemoryTreeStateManager<FolderNode>,

    /**
     * 加载对应子文件夹 回调
     */
    private val loadChild: (FolderNode) -> Unit,

    /**
     * 勾选或取消勾选了对应文件夹 回调
     */
    private val selectFolder: (Boolean, FolderNode) -> Unit
): AbstractTreeViewAdapter<FolderNode>(context, treeStateManager, 5) {

    /**
     * 点击Item的点击事件响应
     */
    private val itemClickAction = View.OnClickListener {
        val node = it.tag as FolderNode
        val nodeInfo = manager.getNodeInfo(node)

        val progressBar = it.findViewById<CircularProgressIndicator>(R.id.progressBar)
        // 如果该Item节点有孩子
        if(node.haveChildren){
            when {
                // 节点下有孩子数据，我们直接展开
                nodeInfo.isWithChildren -> {
                    // 我们把Parent的数据addAnim更改为true，这样下次展开就可以显示展开动画了
                    node.addAnim = true
                    super.handleItemClick(it, node)
                }
                // 如果层级小于4级，我们
                nodeInfo.level < 4 -> {
                    // 展示loading动画
                    progressBar.show()
                    loadChild(node)
                }
                else -> {
                    // 如果level大于4，则不进行child的加载了，并隐藏展开收缩图标，直接作为选择
                    node.haveChildren = false
                    it.findViewById<ImageView>(R.id.iv_folder_status).visibility = View.INVISIBLE
                }
            }
        } else {
            val status = if(node.selected == TreeSelectorHelper.NODE_CHECKED){
                TreeSelectorHelper.NODE_UNCHECKED
            } else {
                TreeSelectorHelper.NODE_CHECKED
            }
            node.selected = status
            selectFolder(status == TreeSelectorHelper.NODE_CHECKED, node)
        }
    }

    private val itemCheckedAction =
        CompoundButton.OnCheckedChangeListener { view, isChecked ->
            val node = view.tag as FolderNode

            node.selected = if(isChecked){
                TreeSelectorHelper.NODE_CHECKED
            } else {
                TreeSelectorHelper.NODE_UNCHECKED
            }
            selectFolder(isChecked, node)
        }

    override fun getNewChildView(parentView: View, treeNodeInfo: TreeNodeInfo<FolderNode>): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = if(treeNodeInfo.id is FolderNode.RootFolderNode){
            layoutInflater.inflate(R.layout.item_folder_root, null)
        } else {
            layoutInflater.inflate(R.layout.item_folder_child, null)
        }
        parentView.setOnClickListener(itemClickAction)
        return updateView(parentView, viewLayout, treeNodeInfo)
    }

    override fun updateView(parentView: View, view: View, treeNodeInfo: TreeNodeInfo<FolderNode>): View {
        view.tag = treeNodeInfo.id
        val folderNode = treeNodeInfo.id
        val root = view as ConstraintLayout
        // 视图分为根Item和子Item两种内容，我们分别绘制
        if(folderNode is FolderNode.RootFolderNode){
            val tvFolderName = root.findViewById<TextView>(R.id.tv_folder_name)
            val ivFolderStatus = root.findViewById<ImageView>(R.id.iv_folder_status)
            val cbSelect = root.findViewById<CheckBox>(R.id.cb_select)
            val progressBar = root.findViewById<CircularProgressIndicator>(R.id.progressBar)

            progressBar.hide()

            cbSelect.tag = treeNodeInfo.id
            tvFolderName.text = folderNode.folderName
            // 为了避免下面的isChecked影响回调进行多余操作处理，我们会先将事件设置为null，然后再恢复
            cbSelect.setOnCheckedChangeListener(null)

            when(folderNode.selected){
                TreeSelectorHelper.NODE_CHECKED ->{
                    cbSelect.isChecked = true
                    tvFolderName.paint.flags = 0
                }
                TreeSelectorHelper.NODE_SELECTED ->{
                    cbSelect.isChecked = false
                    tvFolderName.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
                    tvFolderName.paint.isAntiAlias = true//抗锯齿
                }
                else ->{
                    cbSelect.isChecked = false
                    tvFolderName.paint.flags = 0
                }
            }
            cbSelect.setOnCheckedChangeListener(itemCheckedAction)

            if(folderNode.haveChildren){
                ivFolderStatus.visibility = VISIBLE
                if(treeNodeInfo.isExpanded){
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_expand_tip)
                } else {
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_collapse_tip)
                }

            } else {
                ivFolderStatus.visibility = View.INVISIBLE
            }

        } else {
            folderNode as FolderNode.ChildFolderNode
            val tvFolderName = root.findViewById<TextView>(R.id.tv_folder_name)
            val ivFolderStatus = root.findViewById<ImageView>(R.id.iv_folder_status)
            val cbSelect = root.findViewById<CheckBox>(R.id.cb_select)
            val progressBar = root.findViewById<CircularProgressIndicator>(R.id.progressBar)

            progressBar.hide()

            cbSelect.tag = treeNodeInfo.id
            tvFolderName.text = folderNode.folderName

            // 为了避免下面的isChecked影响回调进行多余操作处理，我们会先将事件设置为null，然后再恢复
            cbSelect.setOnCheckedChangeListener(null)
            when(folderNode.selected){
                TreeSelectorHelper.NODE_CHECKED ->{
                    cbSelect.isChecked = true
                    tvFolderName.paint.flags = 0
                }
                TreeSelectorHelper.NODE_SELECTED ->{
                    cbSelect.isChecked = false
                    tvFolderName.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
                    tvFolderName.paint.isAntiAlias = true//抗锯齿
                }
                else ->{
                    cbSelect.isChecked = false
                    tvFolderName.paint.flags = 0
                }
            }
            cbSelect.setOnCheckedChangeListener(itemCheckedAction)

            if(folderNode.haveChildren){
                ivFolderStatus.visibility = VISIBLE
                if(treeNodeInfo.isExpanded){
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_expand_tip)
                } else {
                    ivFolderStatus.setImageResource(R.drawable.ic_folder_collapse_tip)
                }

            } else {
                ivFolderStatus.visibility = View.INVISIBLE
            }

            // 判断父节点的addAnim是否为true，如果为true我们播放动画
            val parentNode = manager.getParent(folderNode)
            if(parentNode != null && parentNode.addAnim){
                view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                val realHeight = view.measuredHeight
                view.visibility = View.GONE
                view.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, 0
                )
                view.clipToPadding = false
                view.clipChildren = false
                // 使用展开动画
                val animation = ObjectAnimator.ofInt(0, realHeight)
                animation.addUpdateListener {
                    val height = it.animatedValue as Int
                    view.updateLayoutParams<FrameLayout.LayoutParams> {
                        this.height = height
                    }
                }
                animation.addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator) {
                        view.visibility = VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        parentNode.addAnim = false
                        animation.removeAllListeners()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })
                animation.duration = 275
                animation.start()
            } else {
                view.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
        return view
    }

    override fun getItemId(position: Int): Long {
        return getTreeId(position).id
    }
}