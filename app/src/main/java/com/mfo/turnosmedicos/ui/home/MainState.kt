package com.mfo.turnosmedicos.ui.home


sealed class MainState {
    data object Loading: MainState()
    data class Error(val error: String): MainState()
    data object Success: MainState()
}