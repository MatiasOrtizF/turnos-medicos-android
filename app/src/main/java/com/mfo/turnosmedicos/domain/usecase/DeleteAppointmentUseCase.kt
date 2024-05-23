package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(authorization: String, id: Long): Boolean = repository.cancelAppointment(authorization, id)
}