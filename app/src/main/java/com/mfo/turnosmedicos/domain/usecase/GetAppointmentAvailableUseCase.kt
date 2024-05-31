package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetAppointmentAvailableUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, id: Long): AppointmentAvailableResponse? = repository.getAppointmentAvailable(token, id)
}