package com.mfo.turnosmedicos.domain

import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.domain.model.LoginRequest

interface Repository {
    suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse?
}