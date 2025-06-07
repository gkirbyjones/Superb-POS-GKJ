package com.superbexperience.pos

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.superbexperience.pos.presentation.ProductDetailScreen
import com.superbexperience.pos.presentation.ProductListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)

@Serializable
data class ProductDetailDestination(val productId: String)

@Serializable
object ProductListDestination

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
    ) {
        Surface {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = ProductListDestination) {
                composable<ProductListDestination> {
                    ProductListScreen(navigateToDetails = { productId ->
                        navController.navigate(ProductDetailDestination(productId))
                    })
                }

                composable<ProductDetailDestination> { backStackEntry ->
                    ProductDetailScreen(
                        productId = backStackEntry.toRoute<ProductDetailDestination>().productId,
                        navigateBack = {
                            navController.popBackStack()
                        },
                    )
                }
            }
        }
    }
}
