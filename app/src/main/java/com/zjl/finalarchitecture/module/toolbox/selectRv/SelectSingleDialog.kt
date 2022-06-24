package com.zjl.finalarchitecture.module.toolbox.selectRv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.zjl.finalarchitecture.databinding.DialogSelectSingleOrMultiBinding
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectSingleAdapter

/**
 * @author Xiaoc
 * @since 2022-06-24
 *
 * 单选弹窗
 */
class SelectSingleDialog: DialogFragment() {

    private lateinit var mBinding: DialogSelectSingleOrMultiBinding

    private lateinit var singleAdapter: SelectSingleAdapter

    var confirmCallback: (selectData: String) -> Unit = {}

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
        singleAdapter = SelectSingleAdapter()
        mBinding.rvSelect.adapter = singleAdapter

        singleAdapter.setList(generateData())

        mBinding.btnConfirm.setOnClickListener {
            val currentSelectData = singleAdapter.getItemOrNull(singleAdapter.currentSelect) ?: return@setOnClickListener
            confirmCallback(currentSelectData)
            dismiss()
        }
    }

    private fun generateData(): List<String>{
        return listOf(
            "单选-1",
            "单选-2",
            "单选-3",
            "单选-4",
            "单选-5",
            "单选-6",
            "单选-7",
            "单选-8",
            "单选-9",
            "单选-10",
            "单选-11",
            "单选-12",
            "单选-13",
            "单选-14",
            "单选-15",
            "单选-16",
            "单选-17",
            "单选-18",
            "单选-19",
            "单选-20",
            "单选-21"
        )
    }
}