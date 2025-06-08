package com.superbexperience.pos.domain.model

sealed interface ProductFilter {
    data object All : ProductFilter

    data object CategoryProducts : ProductFilter

    data object CheapProducts : ProductFilter
}
