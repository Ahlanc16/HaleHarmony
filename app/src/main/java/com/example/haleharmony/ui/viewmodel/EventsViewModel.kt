package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.haleharmony.HaleHarmonyApplication
import com.example.haleharmony.data.Event
import com.example.haleharmony.data.EventsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EventsScreenUiState(
    val events: List<Event> = listOf()
)

class EventsViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    val uiState: StateFlow<EventsScreenUiState> =
        eventsRepository.getAllEventsStream().map { EventsScreenUiState(events = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EventsScreenUiState()
            )

    fun addEvent(name: String, dateTime: Long, description: String) {
        viewModelScope.launch {
            val newEvent = Event(
                name = name,
                dateTime = dateTime,
                description = description
            )
            eventsRepository.insertEvent(newEvent)
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(eventId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HaleHarmonyApplication)
                val eventsRepository = application.container.eventsRepository
                EventsViewModel(eventsRepository = eventsRepository)
            }
        }
    }
}
