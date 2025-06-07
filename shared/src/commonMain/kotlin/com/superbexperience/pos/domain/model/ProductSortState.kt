package com.superbexperience.pos.domain.model

data class ProductSortState(
    val sortBy: SortBy = SortBy.NAME,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
) {
    enum class SortBy {
        NAME,
        PRICE,
    }

    fun toggleSortOrder(): ProductSortState {
        return copy(sortOrder = sortOrder.toggle())
    }

    fun setSortBy(newSortBy: SortBy): ProductSortState {
        return copy(sortBy = newSortBy, sortOrder = SortOrder.ASCENDING)
    }
}
