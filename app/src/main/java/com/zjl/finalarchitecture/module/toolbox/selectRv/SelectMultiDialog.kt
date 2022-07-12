package com.zjl.finalarchitecture.module.toolbox.selectRv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.DialogSelectSingleOrMultiBinding
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.JavaSelectMultiAdapter
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectMultiAdapter
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectSingleAdapter

/**
 * @author Xiaoc
 * @since 2022-06-24
 *
 * 多选弹窗
 */
class SelectMultiDialog: DialogFragment() {

    private lateinit var mBinding: DialogSelectSingleOrMultiBinding

    private lateinit var multiAdapter: JavaSelectMultiAdapter

    /**
     * 确认后的回调
     */
    var confirmCallback: (selectData: List<String>) -> Unit = {}

    override fun onStart() {
        super.onStart()

        val layoutParams = requireView().layoutParams
        layoutParams.height = getPeekHeight()
        layoutParams.width = getPeekWidth()
        requireView().layoutParams = layoutParams
    }

    private fun getPeekHeight(): Int{
        return resources.displayMetrics.heightPixels / 5 * 3
    }

    private fun getPeekWidth(): Int{
        return resources.displayMetrics.widthPixels / 5 * 4
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogSelectSingleOrMultiBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        multiAdapter = JavaSelectMultiAdapter()

        multiAdapter.setOnItemClickListener { _, itemView, position ->
            // 更新当前的多选状态
            val item = multiAdapter.getItem(position)
            val checkBox = itemView.findViewById<CheckBox>(R.id.cb_select)
            val selected = !checkBox.isChecked
            multiAdapter.setSelectContent(item, selected, position)
        }

        mBinding.rvSelect.adapter = multiAdapter

        multiAdapter.setList(generateData())

        // 确定后将已选择的数据返回
        mBinding.btnConfirm.setOnClickListener {
            val currentSelectData = multiAdapter.currentSelectSet.toList()
            confirmCallback(currentSelectData)
            dismiss()
        }
    }

    private fun generateData(): List<String>{
        return listOf(
            "多选-1",
            "多选-2",
            "多选-3",
            "多选-4",
            "多选-5",
            "多选-6",
            "多选-7",
            "多选-8",
            "多选-9",
            "多选-10",
            "多选-11",
            "多选-12",
            "多选-13",
            "多选-14",
            "多选-15",
            "多选-16",
            "多选-17",
            "多选-18",
            "多选-19",
            "多选-20",
            "多选-21"
        )
    }
}