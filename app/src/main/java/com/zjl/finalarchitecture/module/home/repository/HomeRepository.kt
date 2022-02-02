package com.zjl.finalarchitecture.module.home.repository

import com.zjl.base.ui.UiModel
import com.zjl.finalarchitecture.api.HomeService
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.library_network.client.retrofit
import com.zjl.library_network.transToUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
object HomeRepository {

    private val homeService by lazy {
        retrofit.create(HomeService::class.java)
    }

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun getBanner(): UiModel<List<BannerVO>> {
        return withContext(Dispatchers.IO){
            val result = homeService.getBanner()
            result.transToUiModel()
        }
    }
}