package com.superbexperience.pos.presentation.productlist

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductFilter
import com.superbexperience.pos.domain.model.ProductSortState
import com.superbexperience.pos.domain.model.ProductSortState.SortBy
import com.superbexperience.pos.domain.usecase.FilterProductListUseCase
import com.superbexperience.pos.domain.usecase.GetProductListUseCase
import com.superbexperience.pos.domain.usecase.SearchProductListUseCase
import com.superbexperience.pos.domain.usecase.SortProductListUseCase
import com.superbexperience.pos.presentation.productlist.ProductListState.DisplayingProducts
import com.superbexperience.pos.presentation.productlist.ProductListState.Loading
import com.superbexperience.pos.presentation.productlist.ProductListState.NoAvailableProducts
import com.superbexperience.pos.presentation.productlist.ProductListState.NoMatchingProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class ProductListViewModel(
    getProductListUseCase: GetProductListUseCase,
    private val filterProductListUseCase: FilterProductListUseCase,
    private val searchProductListUseCase: SearchProductListUseCase,
    private val sortProductListUseCase: SortProductListUseCase,
) : ViewModel() {
    private val allProducts = getProductListUseCase()
    private val filter = MutableStateFlow<ProductFilter>(ProductFilter.All)
    private val input = MutableStateFlow(ProductListUserInput())

    @NativeCoroutinesState
    val state: StateFlow<ProductListState> =
        combine(allProducts, input) { products, input ->
            val displayedProducts =
                products
                    .search(input.searchQuery)
                    .filter(input.filter)
                    .sort(input.sortState)
                    .toProductListViewData()

            when {
                products.isEmpty() -> NoAvailableProducts
                displayedProducts.isEmpty() -> NoMatchingProducts(input)
                else -> {
                    DisplayingProducts(
                        products = displayedProducts,
                        input = input,
                    )
                }
            }
        }.catch { e ->
            println("Error getting products to display: $e")
            throw e
        }.stateIn(
            viewModelScope = viewModelScope,
            started =
                SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 1.seconds.inWholeMilliseconds, // e.g. allow for screen rotation
                    replayExpirationMillis = Long.MAX_VALUE,
                ),
            initialValue = Loading,
        )

    private fun updateFilter(newFilter: ProductFilter) =
        filter.update { currentFilter ->
            if (currentFilter != newFilter) {
                input.update { it.copy(filter = newFilter) }
                newFilter
            } else {
                currentFilter
            }
        }

    fun onFilterAllClicked() = updateFilter(ProductFilter.All)

    fun onFilterCategoriesClicked() = updateFilter(ProductFilter.CategoryProducts)

    fun onFilterCheapClicked() = updateFilter(ProductFilter.CheapProducts)

    fun onSearchQueryChanged(query: String) =
        input.update {
            it.copy(searchQuery = query)
        }

    fun onSortByNameClicked() = updateSortState(SortBy.NAME)

    fun onSortByPriceClicked() = updateSortState(SortBy.PRICE)

    private fun updateSortState(selectedSortBy: SortBy) =
        input.update { current ->
            val newSortState =
                when (selectedSortBy) {
                    current.sortState.sortBy -> current.sortState.toggleSortOrder()
                    else -> current.sortState.setSortBy(selectedSortBy)
                }
            current.copy(sortState = newSortState)
        }

    private fun List<Product>.filter(productFilter: ProductFilter): List<Product> {
        return filterProductListUseCase(this, productFilter)
    }

    private fun List<Product>.search(searchText: String): List<Product> {
        return searchProductListUseCase(this, searchText)
    }

    private fun List<Product>.sort(sortState: ProductSortState): List<Product> {
        return sortProductListUseCase(this, sortState)
    }

    private fun List<Product>.toProductListViewData(): List<ProductListViewData> {
        return map { it.toProductListViewData() }
    }

    private fun Product.toProductListViewData(): ProductListViewData {
        return ProductListViewData(
            id = id.id,
            name = name,
            price = price,
        )
    }
}
