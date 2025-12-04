package com.example.haleharmony.data

import java.util.Date
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val date: Date,
    val description: String
)