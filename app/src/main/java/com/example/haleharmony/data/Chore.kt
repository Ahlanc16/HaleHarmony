package com.example.haleharmony.data

import java.util.UUID

data class Chore(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val assignedTo: String,
    val isCompleted: Boolean = false
)