package com.team.app.ui.tictactoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.data.model.tic_tac_toe.GameState
import com.team.app.data.model.tic_tac_toe.Player
import com.team.app.data.repositories.AttributesRepository
import com.team.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val attributesRepo: AttributesRepository
) : ViewModel() {
    val state = MutableStateFlow(GameState())

    fun startGame() {
        state.update {
            it.copy(
                currentPlayer = Player.entries[Random.nextInt(0, 2)],
                board = GameState.emptyBoard(),
                winner = null,
                gameOver = false
            )
        }
    }

    fun humanMove(i: Int, j: Int) {
        // only handle clicks if the human is currently playing
        if (state.value.gameOver || state.value.currentPlayer != Player.HUMAN) return

        // do not replace existing tokens
        if (state.value.board[i][j] != null) return

        state.update {
            val newBoard = it.board.also { board ->
                board[i][j] = Player.HUMAN
            }
            it.copy(
                currentPlayer = Player.BOT,
                board = newBoard
            )
        }

        checkGameOver()
    }

    fun botMove() {
        // 1. Check if BOT is about to win
        // 2. Check if HUMAN is about to win
        // 3. Check if BOT can create a line with 2 tokens
        if (!tryLine(Player.BOT, 2) &&
            !tryLine(Player.HUMAN, 2) &&
            !tryLine(Player.BOT, 1)
        ) randomBotMove()
    }

    private fun tryLine(target: Player, maxTargets: Int): Boolean {
        if (state.value.gameOver) return true

        val board = state.value.board
        var targetCount: Int
        var nullCount: Int
        var blockRow = 0

        for (x in 0..2) {
            // check row x for blocking move
            targetCount = board[x].count { it == target }
            nullCount = board[x].count { it == null }
            if (targetCount == maxTargets && nullCount == 3 - maxTargets) {
                placeBotToken(x, board[x].indexOf(null))
                return true
            }

            // check column x for blocking move
            targetCount = 0
            nullCount = 0
            for (i in 0..2) {
                if (board[i][x] == target) targetCount++
                if (board[i][x] == null) {
                    nullCount++
                    blockRow = i
                }
            }
            if (targetCount == maxTargets && nullCount == 3 - maxTargets) {
                placeBotToken(blockRow, x)
                return true
            }
        }

        // check first diagonal for blocking move
        targetCount = 0
        nullCount = 0
        for (i in 0..2) {
            if (board[i][i] == target) targetCount++
            if (board[i][i] == null) {
                nullCount++
                blockRow = i
            }
        }
        if (targetCount == maxTargets && nullCount == 3 - maxTargets) {
            placeBotToken(blockRow, blockRow)
            return true
        }

        // check second diagonal for blocking move
        targetCount = 0
        nullCount = 0
        for (i in 0..2) {
            if (board[i][2 - i] == target) targetCount++
            if (board[i][2 - i] == null) {
                nullCount++
                blockRow = i
            }
        }
        if (targetCount == maxTargets && nullCount == 3 - maxTargets) {
            placeBotToken(blockRow, 2 - blockRow)
            return true
        }

        return false
    }

    private fun placeBotToken(i: Int, j: Int) {
        viewModelScope.launch {
            delay(500)
            state.update {
                val newBoard = it.board.also { board ->
                    board[i][j] = Player.BOT
                }
                it.copy(
                    currentPlayer = Player.HUMAN,
                    board = newBoard
                )
            }

            checkGameOver()
        }
    }

    private fun randomBotMove() {
        val i = Random.nextInt(0, 3)
        val j = Random.nextInt(0, 3)

        if (state.value.board[i][j] == null) {
            placeBotToken(i, j)
        } else {
            randomBotMove()
        }
    }

    private fun checkGameOver() {
        if (someoneWon() || boardIsFull()) {
            state.update {
                it.copy(gameOver = true)
            }
            if (state.value.winner == Player.HUMAN) {
                viewModelScope.launch {
                    attributesRepo.updateCoins(
                        attributesRepo.getAttributes().coins + Constants.TIC_TAC_TOE_PRIZE
                    )
                }
            }
        }
    }

    private fun someoneWon(): Boolean {
        val board = state.value.board
        for (x in 0..2) {
            // check row x for winning condition
            if (board[x][0] != null && board[x][0] == board[x][1] && board[x][1] == board[x][2]) {
                state.update {
                    it.copy(winner = board[x][0])
                }
                return true
            }

            // check column x for winning condition
            if (board[0][x] != null && board[0][x] == board[1][x] && board[1][x] == board[2][x]) {
                state.update {
                    it.copy(winner = board[0][x])
                }
                return true
            }
        }

        // check diagonals for winning condition
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            state.update {
                it.copy(winner = board[1][1])
            }
            return true
        }
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            state.update {
                it.copy(winner = board[1][1])
            }
            return true
        }

        return false
    }

    private fun boardIsFull(): Boolean {
        for (row in state.value.board) {
            for (token in row) {
                if (token == null) return false
            }
        }
        return true
    }
}