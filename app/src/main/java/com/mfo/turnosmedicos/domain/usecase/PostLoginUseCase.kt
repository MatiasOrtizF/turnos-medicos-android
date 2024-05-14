package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.domain.Repository
import com.mfo.turnosmedicos.domain.model.LoginRequest
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(loginRequest: LoginRequest): LoginResponse? = repository.authenticationUser(loginRequest)
}