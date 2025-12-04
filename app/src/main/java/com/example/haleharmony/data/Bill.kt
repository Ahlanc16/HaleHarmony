package com.example.haleharmony.data

import java.util.Date
import java.util.UUID

data class Bill(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Double,
    val dueDate: Date,
    val isPaid: Boolean = false
)