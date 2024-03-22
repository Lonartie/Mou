@file:OptIn(ExperimentalFoundationApi::class)

package com.team.app.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import kotlinx.coroutines.launch

@Preview
@Composable
fun HomePage(viewModel: HomePageViewModel = hiltViewModel()) {
    val firstStart = viewModel.firstStart.collectAsState(initial = true)
    val attributes = viewModel.attributes.collectAsState(
        initial = Attributes(0, 0, 0, 0)
    )

    LaunchedEffect(Unit) {
        viewModel.onStart(firstStart.value)
        viewModel.setFigureState(attributes.value)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopRow(viewModel)
        },
        bottomBar = {
            BottomRow(viewModel)
        }
    ) { innerPadding ->
        Content(innerPadding, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopRow(viewModel: HomePageViewModel = hiltViewModel()) {
    val attributes = viewModel.attributes.collectAsState(
        initial = Attributes(0, 0, 0, 0)
    )
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
                    progress = attributes.value.happiness.toFloat() / 100,
                    icon = Icons.Rounded.ChildCare,
                    name = "Happiness",
                    color = Color.Yellow,
                )
                AttributeBar(
                    progress = attributes.value.hunger.toFloat() / 100,
                    icon = Icons.Rounded.Fastfood,
                    name = "Hunger",
                    color = Color.hsl(30f, 0.96f, 0.55f, 1f)
                )
                AttributeBar(
                    progress = attributes.value.health.toFloat() / 100,
                    icon = Icons.Rounded.ControlPoint,
                    name = "Health",
                    color = Color.hsl(117f, 0.96f, 0.55f, 1f)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.combinedClickable(
                    onClick = { coro.launch { viewModel.openMoneyScreen() } }
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
                Text(attributes.value.coins.toString() + "€", fontSize = 15.sp)
            }
            Image(
                painterResource(id = R.drawable.shop),
                contentDescription = "Items",
                modifier = Modifier
                    .size(40.dp)
                    .combinedClickable(
                        onClick = { coro.launch { viewModel.openShop() } }
                    )
            )
        }
    })
}

@Preview
@Composable
fun Content(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomePageViewModel = hiltViewModel()
) {

    Background(image = R.drawable.background_evening,
               modifier = Modifier
                   .fillMaxSize()
                   .scale(5f))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 190.dp)
    ) {
        Figure(
            image = viewModel.figureState.intValue,
            modifier = Modifier
                .fillMaxWidth()
        )
        Ground(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(innerPadding),
        text = viewModel.stepCoins.intValue.toString() + " steps taken today",
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}

@Preview
@Composable
fun BottomRow(viewModel: HomePageViewModel = hiltViewModel()) {
    val attributes = viewModel.attributes.collectAsState(
        initial = Attributes(0, 0, 0, 0)
    )
    val currentFood = viewModel.currentFood.collectAsState(
        initial = Item(ItemType.FOOD, "Chicken", 10, 10)
    )
    val currentToy = viewModel.currentToy.collectAsState(
        initial = Item(ItemType.TOY, "Ball", 10, 10)
    )
    val currentMisc = viewModel.currentMisc.collectAsState(
        initial = Item(ItemType.MISC, "Potion", 10, 10)
    )

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
                image = painterResource(R.drawable.chicken_leg),
                scale = 1.75f,
                onClick = {
                    coro.launch {
                        viewModel.giveFood(
                            currentFood.value,
                            attributes.value
                        )
                    }
                },
                onLongClick = { coro.launch { viewModel.selectFood() } }
            )
            VerticalDivider()
            NavigationButton(
                name = "Toys",
                image = painterResource(R.drawable.toy_mouse),
                scale = 1.2f,
                onClick = { coro.launch { viewModel.giveToy(currentToy.value, attributes.value) } },
                onLongClick = { coro.launch { viewModel.selectToy() } }
            )
            VerticalDivider()
            NavigationButton(
                name = "Items",
                image = painterResource(R.drawable.potion),
                scale = 1.2f,
                onClick = {
                    coro.launch {
                        viewModel.giveItem(
                            currentMisc.value,
                            attributes.value
                        )
                    }
                },
                onLongClick = { coro.launch { viewModel.selectItem() } }
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
    image: Painter? = painterResource(id = R.drawable.chicken_leg),
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
        if (image != null) {
            Image(
                image,
                contentDescription = name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            )
        }
        Text(
            text = name,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun Figure(image : Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun Ground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ground),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun Background(image: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = modifier
    )
}
