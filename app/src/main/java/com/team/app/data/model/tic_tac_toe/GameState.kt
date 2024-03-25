package com.team.app.data.model.tic_tac_toe

data class GameState(
    val currentPlayer: Player? = null,
    val board: List<List<Player?>> = emptyBoard(),
    val winner: Player? = null,
    val isDraw: Boolean = false
) {
    companion object {
        fun emptyBoard(): List<List<Player?>> {
            return listOf(
                listOf(null, null, null),
                listOf(null, null, null),
                listOf(null, null, null)
            )
        }
    }
}
