package com.zjl.finalarchitecture.module.toolbox.imeAnim

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentImeAnimBinding
import com.zjl.finalarchitecture.module.toolbox.imeAnim.adapter.ImeMessageAdapter
import com.zjl.finalarchitecture.module.toolbox.imeAnim.data.MessageData
import java.util.UUID

/**
 * @author Xiaoc
 * @since 2022-07-13
 *
 * Ime输入法动画监听示例
 */
class ImeFragment: BaseFragment<FragmentImeAnimBinding, ImeViewModel>() {

    private lateinit var imeAdapter: ImeMessageAdapter

    private var isSelectAll = false

    private var backActionCallback = object: OnBackPressedCallback(false){
        override fun handleOnBackPressed() {
            if(imeAdapter.currentEnableSelected){
                imeAdapter.disableSelect()
            }
        }
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        imeAdapter = ImeMessageAdapter()
        mBinding.rvImeMessage.adapter = imeAdapter

        // 监听返回按键，如果在编辑状态则先处理还原
        requireActivity().onBackPressedDispatcher.addCallback(backActionCallback)

        // 模拟发送内容
        mBinding.btnSend.setOnClickListener {
            val text = mBinding.messageEdittext.text
            if(text.isEmpty()){
                Toast.makeText(requireContext(), "请输入内容！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val messageData = MessageData().apply {
                id = UUID.randomUUID().toString()
                message = text.toString()
                type = MessageData.MESSAGE_TYPE_TEXT_SELF
            }
            imeAdapter.addData(messageData)
            mViewModel.addMessageData(messageData)
        }

        // 取消选择
        mBinding.cancel.setOnClickListener {
            imeAdapter.disableSelect()
        }

        // 全选或全不选
        mBinding.cbAll.setOnClickListener {
            if(isSelectAll){
                imeAdapter.unSelectAll()
                mBinding.cbAll.text = "全选"
            } else {
                imeAdapter.selectAll()
                mBinding.cbAll.text = "全不选"
            }
            isSelectAll = !isSelectAll
        }

        // 监听多选是否开启
        imeAdapter.multiSelectListener = object: ImeMessageAdapter.MultiSelectListener{
            override fun selectStatusChanged(enable: Boolean) {
                // 我们在此更新Toolbar内容，和还原一些状态
                if(enable){
                    mBinding.toolbar.navigationIcon = null
                    mBinding.selectContainerToolbar.visibility = View.VISIBLE
                    mBinding.messageHolder.visibility = View.GONE

                    backActionCallback.isEnabled = true
                } else {
                    mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                    mBinding.selectContainerToolbar.visibility = View.GONE
                    mBinding.messageHolder.visibility = View.VISIBLE

                    mBinding.cbAll.text = "全选"
                    mBinding.cbAll.isChecked = false
                    backActionCallback.isEnabled = false
                }
            }
        }

        // 点击ITEM事件
        imeAdapter.setOnItemClickListener { _, itemView, position ->
            if(imeAdapter.currentEnableSelected){
                // 更新当前的多选状态
                val item = imeAdapter.getItem(position)
                val checkBox = itemView.findViewById<CheckBox>(R.id.cbSelect)
                val selected = !checkBox.isChecked
                imeAdapter.setSelectContent(item, selected, position)
            } else {
                Toast.makeText(requireContext(), "点击了ITEM:$position", Toast.LENGTH_SHORT).show()
            }
        }

        imeAdapter.setOnItemLongClickListener { _, _, _ ->
            // 长按ITEM进行开关多选
            imeAdapter.toggleSelect()
            return@setOnItemLongClickListener true
        }

        // 分以下2个步骤使用IME动画监听动作：

//        /**
//         * 1) 我们所有操作需要在开启了沉浸式状态栏后使用，例如在Activity中使用了 `window.setDecorFitsSystemWindows(false)`
//         * 或者使用其他方式设置了view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN | LAYOUT_FULLSCREEN)
//         * 以便能够让 [WindowInsetsCompat] 能够正确处理内容。不开启沉浸式则无法正确处理
//         *
//         * 在弹出软键盘后，视图的部分内容会被遮盖，我们需要监听整个window的变化，通过 window的insets边衬 来取得对应软键盘(IME)的
//         * 高度，取得软键盘高度后我们去设置整个布局的Padding来达到让布局能够完整显示出来的目的。并且更新整个布局的Padding后我们
//         * 我们会分发window的变化让子视图去适应变化后的布局界面（更详细的内容见具体类）
//         */
//        val deferringInsetsListener = RootViewDeferringInsetsCallback(
//            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//            deferredInsetTypes = WindowInsetsCompat.Type.ime()
//        )
//        // 给根布局设置上WindowInsetsAnimation事件回调来处理键盘弹起时的处理
//        // 同时也要给根布局设置好Window变化的处理回调，以便在Window变化时也进行处理
//        ViewCompat.setWindowInsetsAnimationCallback(mBinding.root, deferringInsetsListener)
//        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root, deferringInsetsListener)
//
//        /**
//         * 2) 对系统的动画做出反应，例如当软键盘弹起时，会有一个软键盘动画
//         * 我们给需要对这些动画做出响应的视图设置 [ViewCompat.setWindowInsetsAnimationCallback] 回调
//         * 以便于对这些动画做出对应响应
//         *
//         * 例如这里我们希望底部的输入栏messageHolder和RecyclerView列表rvImeMessage能够在IME弹起时对其做出动画响应
//         *
//         * 注意： [TranslateDeferringInsetsAnimationCallback]依赖[RootViewDeferringInsetsCallback]
//         * 不然会出现意想不到的问题（这里只是一个简单的例子，当然你可以自己做动画相关的处理）
//         */
//        ViewCompat.setWindowInsetsAnimationCallback(
//            mBinding.messageHolder,
//            TranslateDeferringInsetsAnimationCallback(
//                view = mBinding.messageHolder,
//                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
//                // 如果我们要把Window的Insets边衬变化继续分配到它的子View，我们就使用 DISPATCH_MODE_CONTINUE_ON_SUBTREE
//                // 也就是说如果子View也要设置setWindowInsetsAnimationCallback，那么就传入 DISPATCH_MODE_CONTINUE_ON_SUBTREE
//                // 这里我们不需要，所以使用 DISPATCH_MODE_STOP
//                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP
//            )
//        )
//
//        ViewCompat.setWindowInsetsAnimationCallback(
//            mBinding.rvImeMessage,
//            TranslateDeferringInsetsAnimationCallback(
//                view = mBinding.rvImeMessage,
//                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//                deferredInsetTypes = WindowInsetsCompat.Type.ime()
//            )
//        )
//
//        /**
//         * 3) 其他可选步骤，例如你想根据UI自行控制这些动画
//         * 例如你想滚动屏幕，来动态拖动IME输入法的显示
//         * 或者说通过触摸，将IME输入法展示出来等效果
//         * 此Demo暂时没有做这些内容
//         */
    }

    override fun createObserver() {
        mViewModel.messageList.launchAndCollectIn(viewLifecycleOwner){
            imeAdapter.setList(it)
        }
    }
}