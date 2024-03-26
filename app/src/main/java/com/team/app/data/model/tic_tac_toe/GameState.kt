package com.team.app.data.model.tic_tac_toe

data class GameState(
    val currentPlayer: Player? = null,
    val board: Array<Array<Player?>> = emptyBoard(),
    val winner: Player? = null,
    val gameOver: Boolean = false
) {
    companion object {
        fun emptyBoard(): Array<Array<Player?>> {
            return arrayOf(
                arrayOf(null, null, null),
                arrayOf(null, null, null),
                arrayOf(null, null, null)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (currentPlayer != other.currentPlayer) return false
        if (!board.contentDeepEquals(other.board)) return false
        if (winner != other.winner) return false
        return gameOver == other.gameOver
    }

    override fun hashCode(): Int {
        var result = currentPlayer?.hashCode() ?: 0
        result = 31 * result + board.contentDeepHashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        result = 31 * result + gameOver.hashCode()
        return result
    }
}
