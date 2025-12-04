package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.haleharmony.data.Visitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import java.util.UUID

class VisitorsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VisitorsUiState())
    val uiState: StateFlow<VisitorsUiState> = _uiState.asStateFlow()

    init {
        // data for example
        _uiState.value = VisitorsUiState(
            visitors = listOf(
                Visitor(name = "Aunt Carol", arrivalDate = Date(), departureDate = Date()),
                Visitor(name = "John", arrivalDate = Date(), departureDate = Date())
            )
        )
    }
    // adds visitors
    fun addVisitor(name: String, arrivalDate: Date, departureDate: Date) {
        val newVisitor = Visitor(name = name, arrivalDate = arrivalDate, departureDate = departureDate)
        _uiState.update {
            it.copy(visitors = it.visitors + newVisitor)
        }
    }
    // deletes visitors
    fun deleteVisitor(visitorId: UUID) {
        _uiState.update { currentState ->
            val updatedVisitors = currentState.visitors.filter { it.id != visitorId }
            currentState.copy(visitors = updatedVisitors)
        }
    }
    // shows visitors
    fun showAddVisitorDialog() {
        _uiState.update { it.copy(showAddVisitorDialog = true) }
    }
    // dismisses visitors
    fun dismissAddVisitorDialog() {
        _uiState.update { it.copy(showAddVisitorDialog = false) }
    }
}