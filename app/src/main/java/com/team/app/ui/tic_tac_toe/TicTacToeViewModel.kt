package com.team.app.ui.tic_tac_toe

import androidx.lifecycle.ViewModel
import com.team.app.data.model.tic_tac_toe.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class TicTacToeViewModel : ViewModel() {
    val gameState = MutableStateFlow(GameState)
}