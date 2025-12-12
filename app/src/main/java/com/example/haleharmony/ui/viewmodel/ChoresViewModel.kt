package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.haleharmony.HaleHarmonyApplication
import com.example.haleharmony.data.Chore
import com.example.haleharmony.data.ChoresRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChoresScreenUiState(
    val chores: List<Chore> = listOf(),
    val showAddChoreDialog: Boolean = false,
    val showReassignChoreDialog: Boolean = false,
    val choreToReassign: Chore? = null
)

class ChoresViewModel(private val choresRepository: ChoresRepository) : ViewModel() {

    val uiState: StateFlow<ChoresScreenUiState> =
        choresRepository.getAllChoresStream().map { ChoresScreenUiState(chores = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChoresScreenUiState()
            )

    fun addChore(name: String, assignedTo: String) {
        viewModelScope.launch {
            val newChore = Chore(name = name, assignedTo = assignedTo)
            choresRepository.insertChore(newChore)
        }
    }

    fun deleteChore(choreId: String) {
        viewModelScope.launch {
            choresRepository.deleteChore(choreId)
        }
    }

    fun reassignChore(choreId: String, newAssignee: String) {
        viewModelScope.launch {
            choresRepository.reassignChore(choreId, newAssignee)
        }
    }

    fun toggleChoreCompletion(choreId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            choresRepository.updateChoreCompletion(choreId, isCompleted)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HaleHarmonyApplication)
                val choresRepository = application.container.choresRepository
                ChoresViewModel(choresRepository = choresRepository)
            }
        }
    }
}
