package com.mfo.turnosmedicos.ui.home

import com.mfo.turnosmedicos.data.network.response.UserResponse

sealed class MainState {
    data object Loading: MainState()
    data class Error(val error: String): MainState()
    data class Success(val user: UserResponse): MainState()
}