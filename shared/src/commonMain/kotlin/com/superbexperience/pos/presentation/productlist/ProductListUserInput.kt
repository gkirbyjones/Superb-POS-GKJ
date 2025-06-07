package com.superbexperience.pos.presentation.productlist

import com.superbexperience.pos.domain.model.ProductFilter
import com.superbexperience.pos.domain.model.ProductSortState

data class ProductListUserInput(
    val filter: ProductFilter = ProductFilter.All,
    val searchQuery: String = "",
    val sortState: ProductSortState = ProductSortState(),
)
