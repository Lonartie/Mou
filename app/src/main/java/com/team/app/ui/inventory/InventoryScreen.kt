package com.team.app.ui.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.ui.home.Background
import com.team.app.utils.Constants
import com.team.app.utils.capitalize
import kotlinx.coroutines.launch

@Composable
fun InventoryScreen(
    itemTypes: List<ItemType>,
    onBackClick: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val items = viewModel.getItemsWithTypes(itemTypes)
    val coro = rememberCoroutineScope()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                onBackClick = onBackClick,
                itemTypes = itemTypes
            )
        }
    ) { contentPadding ->
        Background(
            modifier = Modifier
                .fillMaxSize()
                .scale(1.5f),
            image = if (isSystemInDarkTheme()) {
                R.drawable.background_darkmode
            } else {
                R.drawable.background_lightmode
            }
        )

        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            if (items.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_items),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(4.dp)
            ) {
                items(items) {
                    ItemCard(
                        modifier = Modifier.padding(8.dp),
                        inventoryItem = it,
                        onClick = {
                            coro.launch {
                                viewModel.setHotbarItem(it)
                                onBackClick()
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InventoryTopAppBar(
    onBackClick: () -> Unit = {},
    itemTypes: List<ItemType> = listOf(ItemType.FOOD, ItemType.MEDICINE)
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.inventory))

                Spacer(modifier = Modifier.weight(1f))

                var types = itemTypes[0].toString().capitalize()

                for (i in 1..<itemTypes.size) {
                    types += ", ${itemTypes[i].toString().capitalize()}"
                }

                Text(
                    text = types,
                    modifier = Modifier.offset(x = (-12).dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        }
    )
}

@Composable
@Preview
fun ItemCard(
    modifier: Modifier = Modifier,
    inventoryItem: InventoryItem = InventoryItem(
        Item(ItemType.FOOD, "Chicken", 5, 1), 1
    ),
    onClick: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = inventoryItem.item.name,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )

            Box {
                Image(
                    painter = painterResource(
                        id = Constants.getItemResource(inventoryItem.item.name)
                    ),
                    contentDescription = inventoryItem.item.name,
                    modifier = Modifier
                        .scale(1.2f * 0.5f)
                )

                Text(
                    text = inventoryItem.quantity.toString(),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-30).dp, y = 20.dp)
                        .padding(5.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.Red,
                                radius = 10.dp.toPx()
                            )
                        },
                    fontSize = 15.sp
                )
            }

            Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.select_item),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}