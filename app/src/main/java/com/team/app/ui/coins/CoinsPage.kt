package com.team.app.ui.coins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R

@Composable
@Preview
fun CoinsPage(
    goBack: () -> Unit = {},
    openStepOverview: () -> Unit = {},
    openInvestments: () -> Unit = {},
    openTicTacToe: () -> Unit = {},
    viewModel: CoinsPageViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBar(onBackClick = goBack) }
    ) { contentPadding ->
        Content(openStepOverview, openInvestments, openTicTacToe, contentPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.coins),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Content(
    openWalking: () -> Unit = {},
    openInvestments: () -> Unit = {},
    openTicTacToe: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = stringResource(id = R.string.coins_page_description))

        Divider()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = openWalking,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.walking),
                fontSize = 20.sp,
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = openInvestments,

            ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.investments),
                fontSize = 20.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = openTicTacToe,

            ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.tic_tac_toe),
                fontSize = 20.sp
            )
        }
    }
}
