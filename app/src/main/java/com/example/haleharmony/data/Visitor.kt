package com.example.haleharmony.data

import java.util.Date
import java.util.UUID

data class Visitor(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val arrivalDate: Date,
    val departureDate: Date
)