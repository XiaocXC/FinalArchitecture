package com.zjl.module_domain.usecase.banner

import com.zjl.library_network.ApiResult
import com.zjl.module_domain.model.banner.BannerVO
import com.zjl.module_domain.repository.HomeRepository
import com.zjl.module_domain.usecase.SuspendUseCase

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
class HomeBannerUseCase: SuspendUseCase<Unit, List<BannerVO>>() {

    override suspend fun execute(parameters: Unit): ApiResult<List<BannerVO>> {
        return HomeRepository.getBanner()
    }

}