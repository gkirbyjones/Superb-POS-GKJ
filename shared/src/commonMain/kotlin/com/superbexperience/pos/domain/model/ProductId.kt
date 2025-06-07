package com.superbexperience.pos.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ProductId(val id: String) {
    init {
        require(id.isNotEmpty()) {
            "ProductId name shouldn't be empty"
        }
    }
}
