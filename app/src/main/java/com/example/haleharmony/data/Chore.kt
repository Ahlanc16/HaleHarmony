package com.example.haleharmony.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chores")
data class Chore(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val assignedTo: String,
    val isCompleted: Boolean = false
)
