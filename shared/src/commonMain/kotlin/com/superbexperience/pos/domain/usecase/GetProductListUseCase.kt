package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductListUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> = productRepository.getProducts()
}
