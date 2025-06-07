package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductFilter

class FilterProductListUseCase {
    operator fun invoke(
        products: List<Product>,
        filter: ProductFilter = ProductFilter.All,
    ): List<Product> =
        when (filter) {
            ProductFilter.All -> products
            is ProductFilter.CategoryProducts -> products.filter { !it.category.isNullOrBlank() }
            is ProductFilter.CheapProducts -> products.filter { it.price <= filter.maxPrice }
        }
}
