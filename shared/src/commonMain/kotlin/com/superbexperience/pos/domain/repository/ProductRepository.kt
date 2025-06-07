package com.superbexperience.pos.domain.repository

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>

    fun getProduct(productId: ProductId): Flow<Product>
}
