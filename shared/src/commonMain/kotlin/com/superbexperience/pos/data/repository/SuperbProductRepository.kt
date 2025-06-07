package com.superbexperience.pos.data.repository

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import com.superbexperience.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class SuperbProductRepository(
    private val local: ProductLocalDataSource,
    private val remote: RemoteProductDataSource,
) : ProductRepository {
    override fun getProducts(): Flow<List<Product>> {
        return flow {
            val products = remote.fetchProducts()
            if (products.isNotEmpty()) local.saveProducts(products)
            emitAll(local.loadProducts())
        }
    }

    override fun getProduct(productId: ProductId): Flow<Product> {
        return local.loadProduct(productId)
    }
}
