package com.team.app.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.ui.home.Background
import com.team.app.utils.Constants
import com.team.app.utils.capitalize

@Composable
//@Preview
fun ShopPage(
    goBack: () -> Unit,
    viewModel: ShopPageViewModel = hiltViewModel()
) {
    val items = viewModel.itemsFlow.collectAsState(emptyList()).value
    val attributes = viewModel.attributesFlow.collectAsState(null).value

    Scaffold(
        topBar = {
            ShopTopAppBar(
                onBackClick = goBack,
                currentBalance = attributes?.coins
            )
        }
    ) { contentPadding ->
        Background(
            image = R.drawable.background_evening,
            modifier = Modifier
                .fillMaxSize()
                .scale(5f)
        )

        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(4.dp)
            ) {
                items(items) {
                    ItemCard(
                        item = it,
                        onClick = { viewModel.buyItem(it) },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ShopTopAppBar(
    onBackClick: () -> Unit = {},
    currentBalance: Int? = 0
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.shop))
                Row {
                    Text(text = currentBalance.toString())
                    Spacer(modifier = Modifier.size(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.coins),
                        contentDescription = stringResource(id = R.string.coins),
                        modifier = Modifier.size(40.dp)
                    )
                }
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
    item: Item = Item(ItemType.FOOD, "Chicken", 5, 1),
    onClick: () -> Unit = {},
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = item.itemType.toString().capitalize(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Image(
                painter = painterResource(Constants.getItemResource(item.name)),
                contentDescription = item.name,
                modifier = Modifier.scale(1.2f * 0.5f)
            )

            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = item.price.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.coins),
                        contentDescription = stringResource(id = R.string.coins),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}