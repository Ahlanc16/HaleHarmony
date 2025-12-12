package com.example.haleharmony.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dateTime: Long,
    val description: String
)
