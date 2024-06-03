package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetDoctorByIdUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(authorization: String, id: Long): DoctorResponse? = repository.getDoctorById(authorization, id)
}