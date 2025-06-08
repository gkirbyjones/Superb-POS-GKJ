package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductFilter
import com.superbexperience.pos.domain.model.ProductFilter.All
import com.superbexperience.pos.domain.model.ProductFilter.CategoryProducts
import com.superbexperience.pos.domain.model.ProductFilter.CheapProducts

class FilterProductListUseCase {
    operator fun invoke(
        products: List<Product>,
        filter: ProductFilter = All,
    ): List<Product> =
        when (filter) {
            All -> products
            CategoryProducts -> products.filter { !it.category.isNullOrBlank() }
            CheapProducts -> products.filter { it.isCheap }
        }
}
