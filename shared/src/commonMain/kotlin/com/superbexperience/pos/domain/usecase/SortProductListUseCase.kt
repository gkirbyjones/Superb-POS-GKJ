package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.NaturalOrderComparator
import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductSortState
import com.superbexperience.pos.domain.model.ProductSortState.SortBy.NAME
import com.superbexperience.pos.domain.model.ProductSortState.SortBy.PRICE
import com.superbexperience.pos.domain.model.SortOrder.ASCENDING
import com.superbexperience.pos.domain.model.SortOrder.DESCENDING
import com.superbexperience.pos.domain.model.SortOrder.UNORDERED

class SortProductListUseCase(
    private val naturalOrder: NaturalOrderComparator,
) {
    operator fun invoke(
        products: List<Product>,
        sortState: ProductSortState = ProductSortState(),
    ): List<Product> = products.sort(sortState)

    private fun List<Product>.sort(sortState: ProductSortState): List<Product> {
        if (sortState.sortOrder == UNORDERED) return this // No sorting needed

        return when (sortState.sortBy) {
            NAME -> {
                // Kotlin's naturalOrder would give the following results:
                // Product 1
                // Product 100
                // Product 11
                // Product 2
                // So we use a custom comparator to ensure that sorting is done correctly.
                sortedWith(compareBy(naturalOrder) { it.name }).let { sorted ->
                    if (sortState.sortOrder == DESCENDING) sorted.reversed() else sorted
                }
            }

            PRICE -> {
                if (sortState.sortOrder == ASCENDING) {
                    sortedBy { it.price }
                } else {
                    sortedByDescending { it.price }
                }
            }
        }
    }
}
