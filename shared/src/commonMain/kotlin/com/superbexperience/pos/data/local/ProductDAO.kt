package com.superbexperience.pos.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.superbexperience.pos.data.local.adapters.ProductIdAdapter
import com.superbexperience.pos.data.repository.ProductLocalDataSource
import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ProductDAO(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher,
) : ProductLocalDataSource {
    private val database: SuperbPOSDb =
        SuperbPOSDb(
            driver = sqlDriver,
            ProductEntityAdapter = ProductEntity.Adapter(ProductIdAdapter),
        )

    private val productQueries = database.productEntityQueries

    private val allProducts: Flow<List<ProductEntity>> =
        productQueries
            .selectAll()
            .asFlow()
            .mapToList(backgroundDispatcher)
            .flowOn(backgroundDispatcher)

    override fun loadProducts(): Flow<List<Product>> =
        productQueries
            .selectAll()
            .asFlow()
            .mapToList(backgroundDispatcher)
            .map {
                it.map { productEntity ->
                    productEntity.toProduct()
                }
            }
            .flowOn(backgroundDispatcher)

    override fun loadProduct(productId: ProductId) =
        productQueries
            .selectById(productId)
            .asFlow()
            .map {
                it.executeAsOneOrNull()?.let { productEntity ->
                    productEntity.toProduct()
                } ?: throw NoSuchElementException("Product with id $productId not found")
            }
            .flowOn(backgroundDispatcher)

    override fun saveProducts(products: List<Product>) {
        productQueries.transaction {
            products.forEach { product ->
                productQueries.save(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    category = product.category,
                    description = product.productDescription,
                )
            }
        }
    }

    private fun ProductEntity.toProduct(): Product {
        // TODO: OK for single instance, but mapping should be done in a more generic way
        return Product(
            id = this.productId,
            name = this.name,
            price = this.price,
            category = this.category,
            productDescription = this.description,
        )
    }
}
