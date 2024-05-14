package com.mfo.turnosmedicos.domain.model

data class User (
    val id: Long,
    val name: String,
    val lastName: String,
    val dni: Int,
    val email: String
) {
}