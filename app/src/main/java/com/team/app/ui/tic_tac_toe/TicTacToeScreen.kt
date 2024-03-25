package com.team.app.ui.tic_tac_toe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.data.model.tic_tac_toe.Player

@Composable
fun TicTacToeScreen(
    viewModel: TicTacToeViewModel = hiltViewModel()
) {
    val state = viewModel.gameState.collectAsState().value


}

@Composable
@Preview(showBackground = true)
fun Board(
    modifier: Modifier = Modifier.size(300.dp),
    board: List<List<Player?>> = listOf(
        listOf(Player.BOT, Player.BOT, Player.HUMAN),
        listOf(null, null, null),
        listOf(null, Player.HUMAN, null)
    )
) {
    Canvas(modifier = modifier) {
        drawBoard()
        for (i in board.indices) {
            for (j in board[i].indices) {
                drawToken(board[i][j], i, j)
            }
        }
    }
}

private fun DrawScope.drawBoard() {
    drawLine(
        color = Color.Black,
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
        color = Color.Black,
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
        color = Color.Black,
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
        color = Color.Black,
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

private fun DrawScope.drawToken(player: Player?, i: Int, j: Int) {
    val squareSize = size.width / 3f

    when (player) {
        Player.HUMAN -> {
            // Human gets 'X' tokens
            drawLine(
                color = Color.Black,
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
                color = Color.Black,
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
                color = Color.Black,
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