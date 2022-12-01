package com.zjl.finalarchitecture.module.toolbox.palette

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentPaletteBinding

/**
 * @author Xiaoc
 * @since 2022-12-01
 *
 * 取色Fragment
 */
class PaletteFragment: BaseFragment<FragmentPaletteBinding, PaletteViewModel>() {

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){

        }
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.selectPhoto.setOnClickListener {
            // 启动选择图片
            selectImage.launch("image/*")
        }
    }

    override fun createObserver() {

    }
}