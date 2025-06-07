package com.superbexperience.pos.data.local.adapters

import app.cash.sqldelight.ColumnAdapter
import com.superbexperience.pos.domain.model.ProductId

object ProductIdAdapter : ColumnAdapter<ProductId, String> {
    override fun decode(databaseValue: String) = ProductId(databaseValue)

    override fun encode(value: ProductId) = value.id
}
