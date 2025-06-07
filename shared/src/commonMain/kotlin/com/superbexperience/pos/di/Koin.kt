package com.superbexperience.pos.di

import com.superbexperience.pos.data.local.ProductDAO
import com.superbexperience.pos.data.remote.FakeProductRemoteDataSource
import com.superbexperience.pos.data.repository.ProductLocalDataSource
import com.superbexperience.pos.data.repository.RemoteProductDataSource
import com.superbexperience.pos.data.repository.SuperbProductRepository
import com.superbexperience.pos.domain.model.NaturalOrderComparator
import com.superbexperience.pos.domain.repository.ProductRepository
import com.superbexperience.pos.domain.usecase.FilterProductListUseCase
import com.superbexperience.pos.domain.usecase.GetProductDetailUseCase
import com.superbexperience.pos.domain.usecase.GetProductListUseCase
import com.superbexperience.pos.domain.usecase.SearchProductListUseCase
import com.superbexperience.pos.domain.usecase.SortProductListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin() = initKoin(emptyList())

fun initKoin(extraModules: List<Module>) {
    startKoin {
        modules(
            platformModule,
            coreModule,
            *extraModules.toTypedArray(),
        )
    }
}

private val coreModule =
    module {
        // Data
        single<ProductLocalDataSource> { ProductDAO(get(), Dispatchers.IO) }
        single<RemoteProductDataSource> { FakeProductRemoteDataSource() }
        single<ProductRepository> { SuperbProductRepository(get(), get()) }

        // Domain use cases
        factory<FilterProductListUseCase> { FilterProductListUseCase() }
        factory<GetProductDetailUseCase> { GetProductDetailUseCase(get()) }
        factory<GetProductListUseCase> { GetProductListUseCase(get()) }
        factory<SearchProductListUseCase> { SearchProductListUseCase() }
        factory<SortProductListUseCase> { SortProductListUseCase(get()) }

        // utilities
        factory<NaturalOrderComparator> { NaturalOrderComparator() }
    }

expect val platformModule: Module
