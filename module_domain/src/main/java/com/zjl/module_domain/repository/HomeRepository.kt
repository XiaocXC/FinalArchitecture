package com.zjl.module_domain.repository

import com.zjl.library_network.ApiResult
import com.zjl.library_network.client.retrofit
import com.zjl.library_network.exception.ApiException
import com.zjl.module_domain.UiModel
import com.zjl.module_domain.api.HomeService
import com.zjl.module_domain.model.banner.BannerVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
object HomeRepository {

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun getBanner(): UiModel<List<BannerVO>>{
        val result = withContext(Dispatchers.IO){
            val homeService = retrofit.create(HomeService::class.java)
            homeService.getBanner()
        }
        return if(result is ApiResult.Success){
            UiModel.Success(result.data)
        } else {
            val errorResult = result as ApiResult.Failure
            UiModel.Error(ApiException(errorResult.error), null)
        }
    }
}