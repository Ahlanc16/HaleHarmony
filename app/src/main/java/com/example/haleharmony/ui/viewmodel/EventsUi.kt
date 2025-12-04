package com.example.haleharmony.ui.viewmodel

import com.example.haleharmony.data.Event

data class EventsUiState(
    val events: List<Event> = emptyList(),
    val showAddEventDialog: Boolean = false
)