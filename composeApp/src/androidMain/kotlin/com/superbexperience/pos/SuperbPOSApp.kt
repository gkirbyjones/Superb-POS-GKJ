package com.superbexperience.pos

import android.app.Application
import android.content.Context
import com.superbexperience.pos.di.initKoin
import com.superbexperience.pos.presentation.productdetail.ProductDetailViewModel
import com.superbexperience.pos.presentation.productlist.ProductListViewModel
import org.koin.dsl.module

class SuperbPOSApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            listOf(
                module {
                    single<Context> { this@SuperbPOSApp }

                    factory { ProductDetailViewModel(get()) }
                    factory { ProductListViewModel(get(), get(), get(), get()) }
                },
            ),
        )
    }
}
