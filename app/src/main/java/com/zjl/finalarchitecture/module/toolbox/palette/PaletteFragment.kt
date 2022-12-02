package com.zjl.finalarchitecture.module.toolbox.palette

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentPaletteBinding
import com.zjl.finalarchitecture.module.toolbox.palette.adapter.PaletteSchemeAdapter

/**
 * @author Xiaoc
 * @since 2022-12-01
 *
 * 取色Fragment
 */
class PaletteFragment: BaseFragment<FragmentPaletteBinding, PaletteViewModel>() {

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            mBinding.selectPhoto.load(uri)
            mViewModel.resolvePalette(uri)
        }
    }

    private lateinit var adapter: PaletteSchemeAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        adapter = PaletteSchemeAdapter()
        mBinding.rvScheme.adapter = adapter

        mBinding.selectPhoto.setOnClickListener {
            // 启动选择图片
            selectImage.launch("image/*")
        }
    }

    override fun createObserver() {
        mViewModel.paletteList.launchAndCollectIn(this){
            it.onSuccess { data ->
                adapter.setList(data)
            }
        }
    }
}