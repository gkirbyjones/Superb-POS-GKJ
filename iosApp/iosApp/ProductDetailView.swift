//
//  ProductDetailView.swift
//  iosApp
//
//  Created by Graham Kirby-Jones on 05/06/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync
import KMPObservableViewModelSwiftUI


struct ProductDetailView: View {
    @StateViewModel
    var viewModel = ProductDetailViewModel(
        getProductDetailUseCase: KoinDependencies().getProductDetailUseCase
    )

    let productId: String

    var body: some View {
        VStack {
            if let product = viewModel.product {
                ProductDetails(product: product)
            }
        }
        .onAppear {
            viewModel.setId(productId: productId)
        }
    }
}

struct ProductDetails: View {
    var product: ProductDetailViewData
    
    var body: some View {
        ScrollView {
            
            VStack(alignment: .leading, spacing: 6) {
                Text(product.name)
                    .font(.title)
                
                LabeledInfo(label: "Price", data: "€\(product.price)")
            }
            .padding(16)
            
        }
    }
    
    private struct LabeledInfo: View {
        var label: String
        var data: String
        
        var body: some View {
            Spacer()
            Text("**\(label):** \(data)")
        }
    }
}
