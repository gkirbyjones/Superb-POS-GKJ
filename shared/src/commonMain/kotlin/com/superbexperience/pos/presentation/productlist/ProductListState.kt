package com.superbexperience.pos.presentation.productlist

sealed interface ProductListState {
    val isSearchable: Boolean

    data object Loading : ProductListState {
        override val isSearchable: Boolean = false
    }

    data object NoAvailableProducts : ProductListState {
        override val isSearchable: Boolean = false
    }

    data class DisplayingProducts(
        val products: List<ProductListViewData> = emptyList(),
        override val input: ProductListUserInput = ProductListUserInput(),
        override val isSearchable: Boolean = true,
    ) : ProductListState, HasUserInput, IsSortable

    data class Error(
        val message: String? = null,
    ) : ProductListState {
        override val isSearchable: Boolean = false
    }

    data class NoMatchingProducts(
        override val input: ProductListUserInput = ProductListUserInput(),
        override val isSearchable: Boolean = true,
    ) : ProductListState, HasUserInput
}

interface HasUserInput {
    val input: ProductListUserInput
}

interface IsSortable
