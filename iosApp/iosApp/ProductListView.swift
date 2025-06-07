//
//  ProductListView.swift
//  iosApp
//
//  Created by Graham Kirby-Jones on 05/06/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import KMPNativeCoroutinesAsync
import KMPObservableViewModelSwiftUI
import Shared

struct ProductListView: View {
    @StateViewModel
    var viewModel = ProductListViewModel(
        getProductListUseCase: KoinDependencies().getProductListUseCase,
        filterProductListUseCase: KoinDependencies().filterProductListUseCase,
        searchProductListUseCase: KoinDependencies().searchProductListUseCase,
        sortProductListUseCase: KoinDependencies().sortProductListUseCase
    )
    
    @State private var searchText = ""
    
    var body: some View {
        ZStack {
            NavigationStack {
                switch viewModel.state {
                case is ProductListStateLoading: Text("Loading products...")
                    
                case let state as ProductListStateDisplayingProducts:
                    ProductList(products: state.products)
                        .toolbar {
                            ToolbarItemGroup(placement: .navigationBarTrailing) {
                                Button(action: {
                                    viewModel.onSortByNameClicked()
                                }) {
                                    HStack {
                                        Text("Name")
                                        if state.input.sortState.sortBy == .name {
                                            switch state.input.sortState.sortOrder {
                                            case .ascending:
                                                Image(systemName: "arrow.up")
                                                    .imageScale(.small)
                                                    .accessibilityLabel("Sort by Name")
                                            case .descending:
                                                Image(systemName: "arrow.down")
                                                    .imageScale(.small)
                                                    .accessibilityLabel("Sort by Name")
                                            default : EmptyView()
                                            }
                                        }
                                    }
                                }
                                Button(action: {
                                    viewModel.onSortByPriceClicked()
                                }) {
                                    Text("Price")
                                    if state.input.sortState.sortBy == .price {
                                        switch state.input.sortState.sortOrder {
                                        case .ascending:
                                            Image(systemName: "arrow.up")
                                                .imageScale(.small)
                                                .accessibilityLabel("Sort by Price")
                                        case .descending:
                                            Image(systemName: "arrow.down")
                                                .imageScale(.small)
                                                .accessibilityLabel("Sort by Price")
                                        default : EmptyView()
                                        }
                                    }
                                }
                                Menu {
                                    Button(action: {
                                        viewModel.onFilterAllClicked()
                                    }) {
                                        Label("All", systemImage: (state.input.filter is ProductFilterAll) ? "checkmark" : "")
                                    }
                                    Button(action: {
                                        viewModel.onFilterCategoriesClicked()
                                    }) {
                                        Label("Categories", systemImage: (state.input.filter is ProductFilterCategoryProducts) ? "checkmark" : "")
                                    }
                                    Button(action: {
                                        viewModel.onFilterCheapClicked()
                                    }) {
                                        Label("Cheap Products", systemImage: (state.input.filter is ProductFilterCheapProducts) ? "checkmark" : "")
                                    }
                                } label: {
                                    Image(systemName: "line.3.horizontal.decrease")
                                    .font(.caption)
                                    .accessibilityLabel("Filter")
                                }
                            }
                        }
                    
                case let state as ProductListStateError: Text("Error: \(String(describing: state.message))")
                    
                case is ProductListStateNoAvailableProducts: Text("No products available")
                    
                case is ProductListStateNoMatchingProducts: Text("No products found")
                    
                default:
                    Text("Unexpected problem with application, please contact support")
                }
            }
            .searchable(
                if: viewModel.state.isSearchable,
                text: $searchText,
                placement: .toolbar,
                prompt: "Search for products"
            )
            .onChange(of: searchText) { query in
                viewModel.onSearchQueryChanged(query: query)
            }
        }
    }
    
    private struct ProductList: View {
        let products: [ProductListViewData]
        
        var body: some View {
            List(products, id: \.id) { item in
                NavigationLink(destination: ProductDetailView(productId: item.id)) {
                    ProductRow(product: item)
                }
                .buttonStyle(PlainButtonStyle())
            }
        }
    }
    
    private struct ProductRow: View {
        let product: ProductListViewData
        
        var body: some View {
            HStack {
                Text(product.name)
                    .font(.headline)
                Spacer()
                Text(String(format: "€%.2f", product.price))
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            .padding(.vertical, 8)
        }
    }
}

#Preview {
    ProductListView()
}
