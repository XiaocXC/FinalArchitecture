package com.zjl.base.utils

import android.app.Activity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.NavHostFragment

/**
 * @author Xiaoc
 * @since  2022-07-04
 **/
fun Fragment.findNavController() = NavHostFragment.findNavController(this)

@MainThread
public inline fun <reified Args : NavArgs> Fragment.navArgs(): NavArgsLazy<Args> =
    NavArgsLazy(Args::class) {
        arguments ?: throw IllegalStateException("Fragment $this has null arguments")
    }