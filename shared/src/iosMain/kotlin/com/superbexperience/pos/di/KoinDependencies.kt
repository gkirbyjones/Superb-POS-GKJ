package com.superbexperience.pos.di

import com.superbexperience.pos.domain.usecase.FilterProductListUseCase
import com.superbexperience.pos.domain.usecase.GetProductDetailUseCase
import com.superbexperience.pos.domain.usecase.GetProductListUseCase
import com.superbexperience.pos.domain.usecase.SearchProductListUseCase
import com.superbexperience.pos.domain.usecase.SortProductListUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinDependencies : KoinComponent {
    val filterProductListUseCase: FilterProductListUseCase by inject()
    val getProductDetailUseCase: GetProductDetailUseCase by inject()
    val getProductListUseCase: GetProductListUseCase by inject()
    val searchProductListUseCase: SearchProductListUseCase by inject()
    val sortProductListUseCase: SortProductListUseCase by inject()
}
