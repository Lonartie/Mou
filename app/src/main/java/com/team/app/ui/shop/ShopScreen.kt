package com.team.app.ui.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.Toys
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.ItemType
import com.team.app.utils.Constants

@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    shopViewModel: ShopViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { ShopTopAppBar(onBackClick = onBackClick) }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                text = stringResource(id = R.string.shop_prompt),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.size(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
            ) {
                items(ItemType.entries) {
                    ItemCard(
                        it,
                        { shopViewModel.buyItem(it) },
                        Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Shop") },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun ItemCard(
    itemType: ItemType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconVector = when (itemType) {
        ItemType.FOOD -> Icons.Rounded.Fastfood
        ItemType.MEDICINE -> Icons.Rounded.MedicalServices
        ItemType.TOY -> Icons.Rounded.Toys
        ItemType.MISC -> Icons.Rounded.MiscellaneousServices
    }

    val itemPrice = when (itemType) {
        ItemType.FOOD -> Constants.foodPrice
        ItemType.MEDICINE -> Constants.healthBoosterPrice
        ItemType.TOY -> Constants.toyPrice
        ItemType.MISC -> 0 // TODO
    }

    Card(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(2.dp)
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "$itemPrice Coins",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}