package com.superbexperience.pos.data.repository

import com.superbexperience.pos.domain.model.Product

interface RemoteProductDataSource {
    suspend fun fetchProducts(): List<Product>
}
