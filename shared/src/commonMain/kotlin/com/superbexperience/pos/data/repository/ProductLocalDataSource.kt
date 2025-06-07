package com.superbexperience.pos.data.repository

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    fun loadProducts(): Flow<List<Product>>

    fun loadProduct(productId: ProductId): Flow<Product>

    fun saveProducts(products: List<Product>)
}
