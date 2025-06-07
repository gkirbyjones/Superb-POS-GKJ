package com.superbexperience.pos.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.superbexperience.pos.presentation.productlist.ProductListViewData
import com.superbexperience.pos.presentation.productlist.ProductListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(navigateToDetails: (productId: String) -> Unit) {
    val viewModel: ProductListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

//    Scaffold(
//        topBar = {
//            @OptIn(ExperimentalMaterial3Api::class)
//            TopAppBar(
//                title = {}
//            )
//        }
//    ) { paddingValues ->
//        AnimatedContent(
//            targetState = state.products.isNotEmpty(),
//            modifier = Modifier.padding(paddingValues)
//        ) { productsAvailable ->
//            if (productsAvailable) {
//                ProductList(state.products, onItemClick = navigateToDetails)
//            } else {
//                EmptyScreenContent(Modifier.fillMaxSize())
//            }
//        }
//    }
}

@Composable
fun ProductList(
    products: List<ProductListViewData>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        items(products) { product ->
            ProductItem(product, onItemClick)
            HorizontalDivider()
        }
    }
}

@Composable
fun ProductItem(
    product: ProductListViewData,
    onItemClick: (String) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clickable { onItemClick(product.id) }
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "€%.2f".format(product.price),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
