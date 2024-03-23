@file:OptIn(ExperimentalFoundationApi::class)

package com.team.app.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.ControlPoint
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.Attributes
import com.team.app.data.model.Hotbar
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.utils.Constants
import com.team.app.utils.Constants.Companion.INVALID_INVENTORY_ITEM
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    openShop: () -> Unit = {},
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val attributes = viewModel.attributes.collectAsState(
        initial = Attributes(0, 0, 0, 0)
    ).value
    val hotbar = viewModel.hotbar.value

    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopRow(attributes, openShop, viewModel::openMoneyScreen)
        },
        bottomBar = {
            BottomRow(
                hotbar,
                attributes,
                viewModel::giveToy,
                viewModel::giveFood,
                viewModel::giveItem,
                viewModel::selectFood,
                viewModel::selectToy,
                viewModel::selectItem
            )
        }
    ) { innerPadding ->
        Content(innerPadding, viewModel.figureState.value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopRow(
    attributes: Attributes = Attributes(10, 20, 30, 40),
    openShop: suspend () -> Unit = {},
    openMoneyScreen: suspend () -> Unit = {}
) {
    val coro = rememberCoroutineScope()

    TopAppBar(title = {
        Row(
            modifier = Modifier
                .padding(
                    10.dp,
                    0.dp,
                    20.dp,
                    0.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AttributeBar(
                    progress = attributes.happiness.toFloat() / 100,
                    icon = Icons.Rounded.ChildCare,
                    name = "Happiness",
                    color = Color.Yellow,
                )
                AttributeBar(
                    progress = attributes.hunger.toFloat() / 100,
                    icon = Icons.Rounded.Fastfood,
                    name = "Hunger",
                    color = Color.hsl(30f, 0.96f, 0.55f, 1f)
                )
                AttributeBar(
                    progress = attributes.health.toFloat() / 100,
                    icon = Icons.Rounded.ControlPoint,
                    name = "Health",
                    color = Color.hsl(117f, 0.96f, 0.55f, 1f)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.combinedClickable(
                    onClick = { coro.launch { openMoneyScreen() } }
                )
            ) {
                Image(
                    painterResource(id = R.drawable.coins),
                    contentDescription = "Coins",
                    modifier = Modifier
                        .size(30.dp)
                        .graphicsLayer(
                            scaleX = 1.2f,
                            scaleY = 1.2f
                        )
                )
                Text(attributes.coins.toString() + "€", fontSize = 15.sp)
            }
            Image(
                painterResource(id = R.drawable.shop),
                contentDescription = "Items",
                modifier = Modifier
                    .size(40.dp)
                    .combinedClickable(
                        onClick = { coro.launch { openShop() } }
                    )
            )
        }
    })
}

@Preview
@Composable
fun Content(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    figureState: Int = R.drawable.figure_happy
) {
    Background(
        image = R.drawable.background_evening,
        modifier = Modifier
            .fillMaxSize()
            .scale(5f)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Figure(
            image = figureState,
            modifier = Modifier
                .fillMaxWidth()
        )
        Ground(
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}

@Preview
@Composable
fun BottomRow(
    hotbar: Hotbar = Hotbar(
        foodItem = InventoryItem(Item(ItemType.FOOD, "Chicken", 10, 10), 3),
        toyItem = InventoryItem(Item(ItemType.TOY, "Mouse", 5, 5), 2),
        miscItem = InventoryItem(Item(ItemType.MEDICINE, "Health Potion", 15, 15), 1)
    ),
    attributes: Attributes = Attributes(10, 20, 30, 40),
    giveToy: suspend (Item, Attributes) -> Unit = { _, _ -> },
    giveFood: suspend (Item, Attributes) -> Unit = { _, _ -> },
    giveItem: suspend (Item, Attributes) -> Unit = { _, _ -> },
    selectFood: suspend () -> Unit = {},
    selectToy: suspend () -> Unit = {},
    selectItem: suspend () -> Unit = {}
) {
    val coro = rememberCoroutineScope()

    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(
                    20.dp,
                    0.dp,
                    20.dp,
                    0.dp
                )
        ) {
            NavigationButton(
                name = "Food",
                image = painterResource(Constants.getItemResource(hotbar.foodItem.item.name)),
                scale = Constants.getItemScalingFactor(hotbar.foodItem.item.name),
                counter = hotbar.foodItem.quantity,
                onClick = {
                    coro.launch {
                        giveFood(hotbar.foodItem.item, attributes)
                    }
                },
                onLongClick = { coro.launch { selectFood() } }
            )
            VerticalDivider()
            NavigationButton(
                name = "Toys",
                image = painterResource(Constants.getItemResource(hotbar.toyItem.item.name)),
                scale = Constants.getItemScalingFactor(hotbar.toyItem.item.name),
                counter = hotbar.toyItem.quantity,
                onClick = { coro.launch { giveToy(hotbar.toyItem.item, attributes) } },
                onLongClick = { coro.launch { selectToy() } }
            )
            VerticalDivider()
            NavigationButton(
                name = "Items",
                image = painterResource(Constants.getItemResource(hotbar.miscItem.item.name)),
                scale = Constants.getItemScalingFactor(hotbar.miscItem.item.name),
                counter = hotbar.miscItem.quantity,
                onClick = {
                    coro.launch {
                        giveItem(hotbar.miscItem.item, attributes)
                    }
                },
                onLongClick = { coro.launch { selectItem() } }
            )
        }
    }
}

@Preview
@Composable
fun AttributeBar(
    progress: Float = .5f,
    icon: ImageVector = Icons.Rounded.Home,
    name: String = "Home",
    color: Color = Color.Blue
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .weight(1f)
                .clip(MaterialTheme.shapes.medium),
            color = color
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            icon,
            contentDescription = name,
            modifier = Modifier.size(10.dp)
        )
    }
}


@Preview
@Composable
fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun NavigationButton(
    name: String = "Name",
    image: Painter = painterResource(id = R.drawable.chicken_leg),
    counter: Int = 0,
    scale: Float = 1.0f,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box {
            Image(
                image,
                contentDescription = name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(40.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            )

            if (counter != 0) {
                Text(
                    text = counter.toString(),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 5.dp, y = (-15).dp)
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
        }
        Text(
            text = name,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
fun Figure(
    image: Int = R.drawable.figure_happy,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = modifier
    )
}

@Preview
@Composable
fun Ground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ground),
        contentDescription = null,
        modifier = modifier
    )
}

@Preview
@Composable
fun Background(
    image: Int = R.drawable.background_evening,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = modifier
    )
}
