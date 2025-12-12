package com.example.haleharmony.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "visitors")
data class Visitor(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val arrivalDateTime: Long,
    val departureDateTime: Long
)
