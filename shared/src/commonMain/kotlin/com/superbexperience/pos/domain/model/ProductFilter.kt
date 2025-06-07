package com.superbexperience.pos.domain.model

sealed interface ProductFilter {
    data object All : ProductFilter

    data object CategoryProducts : ProductFilter

    data class CheapProducts(val maxPrice: Double = 100.0) : ProductFilter
}
