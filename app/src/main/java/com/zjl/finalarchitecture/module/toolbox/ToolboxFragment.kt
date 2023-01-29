package com.zjl.finalarchitecture.module.toolbox

import android.content.Intent
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.xiaoc.feature_fluid_music.ui.FluidMusicMainActivity
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentToolboxBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections
import com.zjl.finalarchitecture.theme.FinalTheme
import com.zjl.finalarchitecture.theme.ThemeManager

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 工具集Box Fragment
 */
class ToolboxFragment : BaseFragment<FragmentToolboxBinding, EmptyViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.btnMulti.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMultiListFragment())
        }

        mBinding.btnDropPop.setOnClickListener {
        }

        mBinding.btnTreeCheck.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToTreeCheckFragment())
        }

        mBinding.btnDragSimple.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRecyclerViewDragFragment())
        }

        mBinding.btnRvSelect.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSelectSingleOrMultiFragment())
        }

        mBinding.btnImeAnim.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToImeAnimFragment())
        }

        mBinding.btnBgChange.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToBackgroundChangeFragment())
        }

        mBinding.btnFluidMusic.setOnClickListener {
            startActivity(Intent(requireContext(), FluidMusicMainActivity::class.java))
        }

        mBinding.btnShoppingCart.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToShoppingCartFragment())
        }

        mBinding.btnProgressList.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToProgressListFragment())
        }

        mBinding.btnExpandList.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToExpandListFragment2())
        }

        mBinding.btnPalette.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToPaletteFragment())
        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return immersionBar.fitsSystemWindows(true)
    }

    override fun createObserver() {
    }


}