package com.mfo.turnosmedicos.domain.usecase

import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.domain.Repository
import javax.inject.Inject

class GetDoctorBySpecialityUseCase @Inject constructor(private val repository: Repository)  {
    suspend operator fun invoke(authorization: String, speciality: String): List<DoctorResponse>? = repository.getDoctorBySpeciality(authorization, speciality)
}