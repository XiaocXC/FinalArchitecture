package com.zjl.module_domain.repository

import com.zjl.library_network.ApiResult
import com.zjl.library_network.client.retrofit
import com.zjl.module_domain.api.HomeService
import com.zjl.module_domain.model.banner.BannerVO

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
object HomeRepository {

    suspend fun getBanner(): ApiResult<List<BannerVO>>{
        val homeService = retrofit.create(HomeService::class.java)
        return homeService.getBanner()
    }
}