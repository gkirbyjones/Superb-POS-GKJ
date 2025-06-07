package com.superbexperience.pos.domain.usecase

import com.superbexperience.pos.domain.model.Product
import com.superbexperience.pos.domain.model.ProductId
import com.superbexperience.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductDetailUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(productId: ProductId): Flow<Product> = productRepository.getProduct(productId)
}
