package com.team.app.ui.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.utils.Constants

@Composable
fun InventoryScreen(
    itemType: ItemType,
    onBackClick: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    Scaffold (
        topBar = { InventoryTopAppBar(onBackClick = onBackClick) }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                text = stringResource(id = R.string.inventory_prompt),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(viewModel.getItemsWithType(itemType)) {
                    ItemCard(
                        item = viewModel.getItemWithID(it.itemID),
                        onClick = {
                            onBackClick()
                        }
                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Inventory") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    item: Item
) {
    Card(modifier = modifier) {
        Column {
            Text(text = item.name)
            Spacer(modifier = Modifier.size(8.dp))
            Image(
                painter = painterResource(id = Constants.getItemResource(item.name)),
                contentDescription = null,
                modifier = Modifier.scale(Constants.getItemScalingFactor(item.name) * 0.5f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(onClick = onClick) {
                Text(text = stringResource(id = R.string.use_item))
            }
        }
    }
}