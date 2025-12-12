package com.example.haleharmony.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: Double,
    val dueDate: Long,
    val isPaid: Boolean = false
)
