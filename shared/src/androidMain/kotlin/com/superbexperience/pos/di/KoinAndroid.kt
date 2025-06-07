package com.superbexperience.pos.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.superbexperience.pos.data.local.SuperbPOSDb
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single<SqlDriver> {
            val context: android.content.Context = get()
            AndroidSqliteDriver(
                SuperbPOSDb.Schema,
                context,
                "SuperbPOSDb",
            )
        }
    }
