package com.team.app.ui.tic_tac_toe

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
            it.copy(currentPlayer = Player.entries[Random.nextInt(0, 2)])
        }
    }

    fun restartGame() {
        state.update {
            GameState()
        }
        startGame()
    }

    fun humanMove(i: Int, j: Int) {
        println("called humanMove")
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
        printBoard(state.value.board)
    }

    fun botMove() {
        viewModelScope.launch {
            println("called botMove")
            val i = Random.nextInt(0, 3)
            val j = Random.nextInt(0, 3)

            if (state.value.board[i][j] == null) {
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
            } else {
                botMove()
            }
        }
    }

    private fun checkGameOver() {
        println("called checkGameOver")
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
        println("called someoneWon")
        val board = state.value.board
        for (i in 0..2) {
            // check row i for winning condition
            if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                state.update {
                    it.copy(winner = board[i][0])
                }
                return true
            }

            // check column i for winning condition
            if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                state.update {
                    it.copy(winner = board[0][i])
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
        println("called boardIsFull")
        for (row in state.value.board) {
            for (token in row) {
                if (token == null) return false
            }
        }
        return true
    }

    private fun printBoard(board: Array<Array<Player?>>) {
        board.forEach {
            it.forEach { player ->
                print(player?.toString() ?: ".... ")
            }
            println()
        }
    }
}