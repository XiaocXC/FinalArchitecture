package com.zjl.finalarchitecture.module.toolbox.treeCheck.helper

import androidx.annotation.WorkerThread
import com.zjl.finalarchitecture.widget.treeview.InMemoryTreeStateManager
import com.zjl.finalarchitecture.widget.treeview.TreeBuilder
import com.zjl.finalarchitecture.widget.treeview.TreeStateManager

/**
 * @author Xiaoc
 * @since  2023-05-20
 *
 * 一个动态加载的树结构选中查询等内容的帮助类
 **/
abstract class TreeSelectorHelper<T: Any>{

    companion object {
        /**
         * 当前节点处于未选中状态
         */
        const val NODE_UNCHECKED = -1

        /**
         * 当前节点处于半选中状态
         */
        const val NODE_SELECTED = 0

        /**
         * 当前节点处于选中状态
         */
        const val NODE_CHECKED = 1
    }

    val nodeManager = InMemoryTreeStateManager<T>()
    protected val treeBuilder = TreeBuilder(nodeManager)

    /**
     * 加载对应parent节点下的子内容
     * @param parent 父节点
     * @param builder 树构建器
     */
    protected abstract fun loadChildren(parent: T, builder: TreeBuilder<T>, nodeManager: TreeStateManager<T>)

    /**
     * 设置节点的选择状态
     * @param node 节点
     * @param status 选择状态
     */
    protected abstract fun setNodeStatus(node: T, status: Int)

    /**
     * 获取节点的选择状态
     * @param node 节点
     */
    protected abstract fun getNodeStatus(node: T): Int

    /**
     * 添加根节点
     * @param root 根节点
     * @param level 层级
     */
    fun addRoot(root: T, level: Int){
        treeBuilder.sequentiallyAddNextNode(root, level)
    }

    @WorkerThread
    fun loadChildren(parent: T){
        loadChildren(parent, treeBuilder, nodeManager)
    }

    /**
     * 节点选中变化
     * @param node 变化的节点
     * @param checked 选中状态
     */
    @WorkerThread
    fun applyNodeCheckedChanged(node: T, checked: Boolean){
        val status = if(checked){
            NODE_CHECKED
        } else {
            NODE_UNCHECKED
        }
        // 找到该父节点的所有子节点
        nodeChildrenCheckedChanged(node, status)
        // 找到该节点的父节点
        nodeParentCheckedChanged(node, status)
        // 刷新视图
        nodeManager.refresh()
    }

    protected open fun nodeChildrenCheckedChanged(node: T, status: Int){
        val children = nodeManager.getChildren(node)
        if(children.isNullOrEmpty()){
            return
        }
        children.forEach { child ->
            setNodeStatus(child, status)
            // 继续往下递归，父节点的选中状态决定着子节点的选中状态
            nodeChildrenCheckedChanged(child, status)
        }

    }

    private fun nodeParentCheckedChanged(node: T, status: Int){
        val parentNode = nodeManager.getParent(node) ?: return

        // 获得父节点下的所有子节点，我们判断其选中的数量
        val parentChildrenNode = nodeManager.getChildren(parentNode)
        var childHasSelected = false
        for(child in parentChildrenNode){
            val childStatus = getNodeStatus(child)
            if(childStatus != NODE_UNCHECKED){
                childHasSelected = true
                break
            }
        }

        // 如果子节点下有选中的内容，我们将父节点的状态设置为SELECTED
        if(childHasSelected){
            setNodeStatus(parentNode, NODE_SELECTED)
        } else {
            setNodeStatus(parentNode, status)
        }
        nodeParentCheckedChanged(parentNode, status)
    }


}