package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.haleharmony.HaleHarmonyApplication
import com.example.haleharmony.data.Visitor
import com.example.haleharmony.data.VisitorsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date


data class VisitorsScreenUiState(
    val visitors: List<Visitor> = listOf(),
    val showAddVisitorDialog: Boolean = false
)

class VisitorsViewModel(private val visitorsRepository: VisitorsRepository) : ViewModel() {

    // The UI state is now directly from the repository's data flow
    val uiState: StateFlow<VisitorsScreenUiState> = 
        visitorsRepository.getAllVisitorsStream().map { VisitorsScreenUiState(visitors = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = VisitorsScreenUiState()
            )

    fun addVisitor(name: String, arrivalDate: Date, departureDate: Date) {
        viewModelScope.launch {
            val newVisitor = Visitor(
                name = name,
                // Convert Dates to Longs for database storage
                arrivalDateTime = arrivalDate.time,
                departureDateTime = departureDate.time
            )
            visitorsRepository.insertVisitor(newVisitor)
        }
    }

    fun deleteVisitor(visitorId: String) {
        viewModelScope.launch {
            visitorsRepository.deleteVisitor(visitorId)
        }
    }
    /**
     * Factory for creating [VisitorsViewModel] instances.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HaleHarmonyApplication)
                val visitorsRepository = application.container.visitorsRepository
                VisitorsViewModel(visitorsRepository = visitorsRepository)
            }
        }
    }
}
