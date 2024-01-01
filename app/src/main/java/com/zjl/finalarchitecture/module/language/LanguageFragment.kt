package com.zjl.finalarchitecture.module.language

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentLanguageBinding
import java.util.Locale

/**
 * @author Xiaoc
 * @since  2024-01-01
 **/
class LanguageFragment: BaseFragment<FragmentLanguageBinding, EmptyViewModel>() {

    private var currentSelectLanguage: String? = null

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbarLanguage.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val languageList = mutableListOf<Pair<String,String>>(
            Pair("zh", "简体中文"), Pair("en", "English"), Pair("ja", "Japanese")
        )

        currentSelectLanguage = if (!AppCompatDelegate.getApplicationLocales().isEmpty) {
            AppCompatDelegate.getApplicationLocales()[0]?.language
        } else {
            Locale.getDefault().language
        }

        val adapter = object: BaseQuickAdapter<Pair<String,String>, BaseViewHolder>(R.layout.item_language_select){
            override fun convert(holder: BaseViewHolder, item: Pair<String,String>) {
                holder.setText(R.id.tvLanguageTitle, item.second)
                val cardView = holder.getView<MaterialCardView>(R.id.languageCard)
                if(currentSelectLanguage == item.first){
                    cardView.setCardBackgroundColor(MaterialColors.getColor(holder.itemView, R.attr.colorPrimary))
                } else {
                    cardView.setCardBackgroundColor(Color.TRANSPARENT)
                }
                cardView.setOnClickListener {
                    val localeList = LocaleListCompat.forLanguageTags(item.first)
                    AppCompatDelegate.setApplicationLocales(localeList)
                }
            }
        }
        mBinding.rvLanguages.adapter = adapter

        adapter.setNewInstance(languageList)
    }

    override fun createObserver() {

    }
}