package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.finalarchitecture.R

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:58
 */
class AskFragment: Fragment() {

    companion object {
        fun newInstance() = AskFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ask, container, false)
    }


}