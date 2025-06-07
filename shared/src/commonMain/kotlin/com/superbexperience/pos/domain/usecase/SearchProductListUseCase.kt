package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.Product

class SearchProductListUseCase {
    operator fun invoke(
        products: List<Product>,
        searchText: String = "",
    ): List<Product> = products.search(searchText)

    private fun List<Product>.search(searchText: String): List<Product> {
        if (searchText.isBlank()) return this

        // We could also move this logic to the repository/database if we need to optimize it further
        return filter { it.name.contains(searchText, ignoreCase = true) }
    }
}
