package com.zjl.module_domain.repository

import com.zjl.base.ui.UiModel
import com.zjl.library_network.client.retrofit
import com.zjl.library_network.transToUiModel
import com.zjl.module_domain.api.HomeService
import com.zjl.module_domain.model.banner.BannerVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
@Deprecated("请暂时使用app层的")
object HomeRepository {

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun getBanner(): UiModel<List<BannerVO>> {
        val result = withContext(Dispatchers.IO){
            val homeService = retrofit.create(HomeService::class.java)
            homeService.getBanner()
        }
        return result.transToUiModel()
    }
}