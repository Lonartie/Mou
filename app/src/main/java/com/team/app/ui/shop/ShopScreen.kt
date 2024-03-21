package com.team.app.ui.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Toys
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.team.app.data.database.model.ItemType

@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    //shopViewModel: ShopViewModel = hiltViewModel()
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {
        items(ItemType.entries) {
            ItemCard(it, { /* TODO */ }, Modifier.padding(4.dp))
        }
    }
}

@Composable
fun ItemCard(
    itemType: ItemType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconVector = when (itemType) {
        ItemType.FOOD -> Icons.Rounded.Fastfood
        ItemType.HEALTH_BOOSTER -> Icons.Rounded.MedicalServices
        ItemType.TOY -> Icons.Rounded.Toys
    }

    Card(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Box {
            Icon(imageVector = iconVector, contentDescription = null)
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShopScreenPreview() {
    AppTheme {
        ShopScreen()
    }
}