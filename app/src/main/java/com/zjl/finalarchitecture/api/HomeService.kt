package com.zjl.finalarchitecture.api

import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.library_network.ApiResult
import retrofit2.http.GET

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
interface HomeService {

    @GET("banner/json")
    suspend fun getBanner(): ApiResult<List<BannerVO>>
}