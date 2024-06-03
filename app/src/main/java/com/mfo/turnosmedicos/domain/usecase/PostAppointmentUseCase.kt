package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.Repository
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import org.json.JSONObject
import javax.inject.Inject

class PostAppointmentUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String ,appointmentRequest: AppointmentRequest): AppointmentResponse? = repository.addAppointment(token, appointmentRequest)
}