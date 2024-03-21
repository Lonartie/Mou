@file:OptIn(ExperimentalFoundationApi::class)

package com.team.app.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.Architecture
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.ControlPoint
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.Attributes

@Preview
@Composable
fun HomePage(viewModel: HomePageViewModel = hiltViewModel()) {
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
    val attributes = viewModel.attributes.value
    TopAppBar(title = {
        Row(
            modifier = Modifier
                .padding(10.dp, 0.dp, 20.dp, 0.dp)
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
                    onClick = viewModel::openMoneyScreen
                )
            ) {
                Icon(
                    Icons.Rounded.MonetizationOn,
                    contentDescription = "Coins",
                    modifier = Modifier.size(30.dp)
                )
                Text(attributes.coins.toString() + "€", fontSize = 15.sp)
            }
            Icon(
                Icons.Rounded.ShoppingBasket,
                contentDescription = "Items",
                modifier = Modifier.size(40.dp).combinedClickable(
                    onClick = viewModel::openShop
                )
            )
        }
    })
}

@Preview
@Composable
fun Content(innerPadding: PaddingValues = PaddingValues(0.dp), viewModel: HomePageViewModel = hiltViewModel()) {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(innerPadding),
        text = "Homepage",
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}

@Preview
@Composable
fun BottomRow(viewModel: HomePageViewModel = hiltViewModel()) {
    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ) {
            NavigationButton(
                name = "Food",
                icon = Icons.Rounded.Fastfood,
                onClick = viewModel::giveFood,
                onLongClick = viewModel::selectFood
            )
            VerticalDivider()
            NavigationButton(
                name = "Games",
                icon = Icons.Rounded.Games,
                onClick = viewModel::giveToy,
                onLongClick = viewModel::selectToy
            )
            VerticalDivider()
            NavigationButton(
                name = "Items",
                icon = Icons.Rounded.Architecture,
                onClick = viewModel::giveItem,
                onLongClick = viewModel::selectItem
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
    icon: ImageVector? = null,
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
        if (icon != null) {
            Icon(
                icon,
                contentDescription = name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(
            text = name,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}