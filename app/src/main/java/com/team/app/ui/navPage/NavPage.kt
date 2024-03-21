package com.team.app.ui.navPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NavPage(viewModel: NavPageViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coro = rememberCoroutineScope()

    val toggleDrawer: () -> Unit = {
        coro.launch {
            if (drawerState.isClosed) drawerState.open() else drawerState.close()
        }
    }

    Scaffold(

        /* TOP BAR */
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.semantics { testTag = "TitleBarText" },
                        text = viewModel.getCurrentTitle()
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics { testTag = "NavDrawerButton" },
                        onClick = { toggleDrawer() }) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Rounded.Favorite, contentDescription = "Heart")
                    }
                }
            )
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .requiredWidth(320.dp)
                        .padding(innerPadding)
                ) {
                    DrawerContent(innerPadding, viewModel, toggleDrawer)
                }
            },
            gesturesEnabled = true,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                viewModel.getActivePage()()
            }
        }
    }
}

@Preview
@Composable
fun DrawerContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: NavPageViewModel = hiltViewModel(),
    toggleDrawer: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        for ((type, page) in viewModel.pageMap) {
            PanelButton(
                name = page.name,
                icon = page.icon,
                pageType = type,
                viewModel = viewModel,
                toggleDrawer = toggleDrawer
            )

            Divider()
        }
    }
}

@Preview
@Composable
fun PanelButton(
    name: String = "NAME",
    icon: ImageVector = Icons.Rounded.Menu,
    pageType: NavPageViewModel.PageType = NavPageViewModel.PageType.HOME,
    viewModel: NavPageViewModel = hiltViewModel(),
    toggleDrawer: () -> Unit = {}
) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
        onClick = { toggleDrawer(); viewModel.setActivePage(pageType) }) {
        Row {
            Icon(
                icon,
                contentDescription = name,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}