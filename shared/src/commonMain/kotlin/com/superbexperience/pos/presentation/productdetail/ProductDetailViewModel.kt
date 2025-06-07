package com.superbexperience.pos.presentation.productdetail

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import com.superbexperience.pos.domain.usecase.GetProductDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.seconds

class ProductDetailViewModel(private val getProductDetailUseCase: GetProductDetailUseCase) : ViewModel() {
    private val productIdFlow = MutableStateFlow<ProductId?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    @NativeCoroutinesState
    val product: StateFlow<ProductDetailViewData?> =
        productIdFlow
            .flatMapLatest { productId ->
                val id = productId ?: return@flatMapLatest flowOf(null)
                getProductDetailUseCase(id)
                    .map { product ->
                        product.toProductDetailViewData()
                    }
            }
            .stateIn(
                viewModelScope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(
                        stopTimeoutMillis = 1.seconds.inWholeMilliseconds, // e.g. allow for screen rotation
                        replayExpirationMillis = Long.MAX_VALUE,
                    ),
                initialValue = null,
            )

    fun setId(productId: String) {
        this.productIdFlow.value = ProductId(productId)
    }

    private fun Product.toProductDetailViewData(): ProductDetailViewData {
        return ProductDetailViewData(
            id = id.id,
            name = name,
            price = price,
        )
    }
}
