package com.superbexperience.pos.domain.model

/**
 * Represents a product.
 * Requirement: Defines a Product model ( name, price, id, optional category or description).
 *
 * Have taken that to imply that the optional category or description should not be used together.
 * I'm assuming that this is a partial implementation of a product model, and that additional
 * requirements such as stock management, images, etc. will be added later.
 *
 * @property id Unique identifier for the product (no assumption made wrt SKU).
 * @property name Display name of the product.
 * @property price Current price of the product in EUR.
 * @property category Optional category details for the product.
 * @property productDescription Optional detailed description of the product.
 */
data class Product(
    val id: ProductId, // TODO: consider using a String and/or typealias for id (type wiped when presented to Swift), but ProductId is more type-safe
    val name: String,
    val price: Double,
    val productDescription: String? = null, // not called 'description' to avoid name mangling when presented to Swift
    val category: String? = null,
) {
    private val maximumCheapPrice = 100.0

    val isCheap: Boolean
        get() = price <= maximumCheapPrice

    init {
        // TODO: consider encapsulating validation in a separate class or function
        //  report all failing conditions at once instead of throwing on the first failure
        require(name.isNotBlank()) {
            "Product name shouldn't be empty"
        }

        require(price >= 0) {
            "Product price should be non-negative"
        }

        category?.let {
            require(productDescription.isNullOrBlank()) {
                "Product cannot have both description and category"
            }
            require(it.isNotBlank()) {
                "Product category shouldn't be empty"
            }
        }

        productDescription?.let {
            require(category.isNullOrBlank()) {
                "Product cannot have both description and category"
            }
            require(it.isNotBlank()) {
                "Product description shouldn't be empty"
            }
        }
    }
}
