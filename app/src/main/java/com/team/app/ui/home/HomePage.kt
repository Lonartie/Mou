package com.team.app.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Architecture
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomePage(viewModel: HomePageViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Attributes")
                    Text("Coins")
                    Text("Shop")
                }
            })
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    NavigationButton(
                        name = "Food",
                        icon = Icons.Rounded.Fastfood
                    )
                    VerticalDivider()
                    NavigationButton(
                        name = "Games",
                        icon = Icons.Rounded.Games
                    )
                    VerticalDivider()
                    NavigationButton(
                        name = "Items",
                        icon = Icons.Rounded.Architecture
                    )
                }
            }
        }
    ) { innerPadding ->
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

@Preview
@Composable
fun NavigationButton(
    name: String = "Name",
    icon: ImageVector? = null,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
    ) {
        Row {
            if (icon != null) {
                Icon(
                    icon!!,
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
}