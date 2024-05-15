package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String): UserResponse? = repository.getUserInfo(token)
}