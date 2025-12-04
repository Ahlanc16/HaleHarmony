package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.haleharmony.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import java.util.UUID

class EventsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        // example data
        _uiState.value = EventsUiState(
            events = listOf(
                Event(name = "Movie Night", date = Date(), description = "Watch the new blockbuster"),
                Event(name = "Game Night", date = Date(), description = "Play some board games")
            )
        )
    }
// adds events
    fun addEvent(name: String, date: Date, description: String) {
        val newEvent = Event(name = name, date = date, description = description)
        _uiState.update {
            it.copy(events = it.events + newEvent)
        }
    }
// delete events
    fun deleteEvent(eventId: UUID) {
        _uiState.update { currentState ->
            val updatedEvents = currentState.events.filter { it.id != eventId }
            currentState.copy(events = updatedEvents)
        }
    }
//shows everything
    fun showAddEventDialog() {
        _uiState.update { it.copy(showAddEventDialog = true) }
    }

    fun dismissAddEventDialog() {
        _uiState.update { it.copy(showAddEventDialog = false) }
    }
}