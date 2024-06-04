package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(val repository: Repository) {
    suspend operator fun invoke(token: String): List<AppointmentResponse>? = repository.getHistory(token)
}