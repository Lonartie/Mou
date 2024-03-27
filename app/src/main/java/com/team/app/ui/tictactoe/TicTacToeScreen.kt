package com.team.app.ui.tictactoe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.R
import com.team.app.data.model.tic_tac_toe.GameState
import com.team.app.data.model.tic_tac_toe.Player
import com.team.app.ui.common.TopAppBar
import com.team.app.utils.Constants

@Composable
fun TicTacToeScreen(
    goBack: () -> Unit,
    viewModel: TicTacToeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = stringResource(id = R.string.tic_tac_toe), onBackClick = goBack)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .offset(y = (-40).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.currentPlayer != null) {
                GameInfoBox(state = state)
                Spacer(modifier = Modifier.size(40.dp))
            }

            Board(
                state = state,
                onClick = viewModel::humanMove
            )

            if (!state.gameOver && state.currentPlayer == Player.BOT) viewModel.botMove()

            if (state.currentPlayer == null || state.gameOver) {
                Spacer(modifier = Modifier.size(40.dp))
                Button(
                    onClick = viewModel::startGame,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (state.currentPlayer == null) {
                            stringResource(id = R.string.ttt_start)
                        } else {
                            stringResource(id = R.string.ttt_new_game)
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GameInfoBox(
    modifier: Modifier = Modifier,
    state: GameState = GameState()
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        val infoString = if (!state.gameOver && state.currentPlayer == Player.BOT) {
            stringResource(R.string.ttt_bot_playing)
        } else if (!state.gameOver && state.currentPlayer == Player.HUMAN) {
            stringResource(id = R.string.ttt_human_playing)
        } else if (state.winner == Player.HUMAN) {
            stringResource(
                R.string.ttt_human_won_formatted, Constants.TIC_TAC_TOE_PRIZE
            )
        } else if (state.winner == Player.BOT) {
            stringResource(id = R.string.ttt_bot_won)
        } else {
            stringResource(id = R.string.ttt_draw)
        }

        Text(
            text = infoString,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(16.dp)
        )

    }
}

@Composable
@Preview(showBackground = true)
fun Board(
    modifier: Modifier = Modifier,
    state: GameState = GameState(
        board = arrayOf(
            arrayOf(Player.BOT, Player.BOT, null),
            arrayOf(null, Player.HUMAN, Player.BOT),
            arrayOf(null, Player.HUMAN, null)
        )
    ),
    onClick: (Int, Int) -> Unit = { _, _ -> }
) {
    val color = MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .size(300.dp)
                .pointerInput(true) {
                    detectTapGestures {
                        val i = 3 * it.y.toInt() / size.height
                        val j = 3 * it.x.toInt() / size.width

                        onClick(i, j)
                    }
                }
                .padding(4.dp)
        ) {
            drawBoard(color = color)
            for (i in state.board.indices) {
                for (j in state.board[i].indices) {
                    drawToken(state.board[i][j], i, j, color)
                }
            }
        }
    }
}

private fun DrawScope.drawBoard(color: Color) {
    drawLine(
        color = color,
        start = Offset(
            x = size.width / 3f,
            y = 0f
        ),
        end = Offset(
            x = size.width / 3f,
            y = size.height
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(
            x = size.width * 2 / 3f,
            y = 0f
        ),
        end = Offset(
            x = size.width * 2 / 3f,
            y = size.height
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(
            x = 0f,
            y = size.height / 3f
        ),
        end = Offset(
            x = size.width,
            y = size.height / 3f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(
            x = 0f,
            y = size.height * 2 / 3f
        ),
        end = Offset(
            x = size.width,
            y = size.height * 2 / 3f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawToken(player: Player?, i: Int, j: Int, color: Color) {
    val squareSize = size.width / 3f

    when (player) {
        Player.HUMAN -> {
            // Human gets 'X' tokens
            drawLine(
                color = color,
                start = Offset(
                    x = squareSize / 4f + squareSize * j,
                    y = squareSize / 4f + squareSize * i
                ),
                end = Offset(
                    x = squareSize * 3 / 4f + squareSize * j,
                    y = squareSize * 3 / 4f + squareSize * i
                ),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = color,
                start = Offset(
                    x = squareSize * 3 / 4f + squareSize * j,
                    y = squareSize / 4f + squareSize * i
                ),
                end = Offset(
                    x = squareSize / 4f + squareSize * j,
                    y = squareSize * 3 / 4f + squareSize * i
                ),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        Player.BOT -> {
            // Bot gets 'O' tokens
            drawCircle(
                color = color,
                radius = squareSize / 4f,
                center = Offset(
                    x = squareSize / 2f + squareSize * j,
                    y = squareSize / 2f + squareSize * i
                ),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        null -> return
    }
}