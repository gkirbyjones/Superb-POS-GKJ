package com.superbexperience.pos.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.superbexperience.pos.data.local.SuperbPOSDb
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

// fun initKoinIos(
//    userDefaults: NSUserDefaults,
//    appInfo: AppInfo,
//    doOnStartup: () -> Unit
// ): KoinApplication = initKoin(
//    module {
//        single<Settings> { NSUserDefaultsSettings(userDefaults) }
//        single { appInfo }
//        single { doOnStartup }
//    }
// )

actual val platformModule =
    module {
        single<SqlDriver> { NativeSqliteDriver(SuperbPOSDb.Schema, "SuperbPOSDb") }

        single { Darwin.create() }

//    single { BreedViewModel(get(), getWith("BreedViewModel")) }
    }

// // Access from Swift to create a logger
// @Suppress("unused")
// fun Koin.loggerWithTag(tag: String) = get<Logger>(qualifier = null) { parametersOf(tag) }
//
// @Suppress("unused") // Called from Swift
// object KotlinDependencies : KoinComponent {
//    fun getBreedViewModel() = getKoin().get<BreedViewModel>()
// }
