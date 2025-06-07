package com.superbexperience.pos.data.remote

import com.superbexperience.pos.data.repository.RemoteProductDataSource
import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import kotlinx.coroutines.delay

class FakeProductRemoteDataSource : RemoteProductDataSource {
    override suspend fun fetchProducts(): List<Product> {
        delay(800) // Simulate network delay
        return createFakeProducts(20, 5)
    }

    private fun createFakeProducts(
        count: Int,
        categoryCount: Int,
    ): List<Product> {
        val describedProducts =
            List(count) { index ->
                Product(
                    id = ProductId(index.toString()),
                    name = "Product $index",
                    price = (index + 1) * 10.0,
                    productDescription = "Description for Product $index",
                )
            }
        val categorizedProducts = mutableListOf<Product>()

        for (index in count until count + categoryCount) {
            categorizedProducts.add(
                Product(
                    id = ProductId((index).toString()),
                    name = "Product $index",
                    price = (index + 1) * 15.0,
                    category = "Category $index",
                ),
            )
        }
        return describedProducts + categorizedProducts
    }
}
