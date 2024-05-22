package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetAllAppointmentUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(authorization: String): List<AppointmentResponse>? = repository.getAllAppointment(authorization)
}