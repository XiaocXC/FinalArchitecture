package com.zjl.module_domain.api

import com.zjl.library_network.ApiResult
import com.zjl.module_domain.model.banner.BannerVO
import retrofit2.http.GET

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
@Deprecated("请暂时使用app层的")
interface HomeService {

    @GET("banner/json")
    suspend fun getBanner(): ApiResult<List<BannerVO>>
}