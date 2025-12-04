package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.haleharmony.data.Chore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ChoresViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChoresUiState())
    val uiState: StateFlow<ChoresUiState> = _uiState.asStateFlow()

    init {
        // example data
        _uiState.value = ChoresUiState(
            chores = listOf(
                Chore(name = "Take out the trash", assignedTo = "Alex"),
                Chore(name = "Do the dishes", assignedTo = "Ben", isCompleted = true),
                Chore(name = "Clean the bathroom", assignedTo = "Charlie")
            )
        )
    }
    // adding chores
    fun addChore(name: String, assignedTo: String) {
        val newChore = Chore(name = name, assignedTo = assignedTo)
        _uiState.update {
            it.copy(chores = it.chores + newChore)
        }
    }
    // deleting chores
    fun deleteChore(choreId: UUID) {
        _uiState.update { currentState ->
            val updatedChores = currentState.chores.filter { it.id != choreId }
            currentState.copy(chores = updatedChores)
        }
    }
    // reassign to another person
    fun reassignChore(choreId: UUID, newAssignee: String) {
        _uiState.update { currentState ->
            val updatedChores = currentState.chores.map {
                if (it.id == choreId) {
                    it.copy(assignedTo = newAssignee)
                } else {
                    it
                }
            }
            currentState.copy(chores = updatedChores)
        }
    }
    // tells user if its complete or not
    fun toggleChoreCompletion(choreId: UUID) {
        _uiState.update { currentState ->
            val updatedChores = currentState.chores.map {
                if (it.id == choreId) {
                    it.copy(isCompleted = !it.isCompleted)
                } else {
                    it
                }
            }
            currentState.copy(chores = updatedChores)
        }
    }

    fun showAddChoreDialog() {
        _uiState.update { it.copy(showAddChoreDialog = true) }
    }

    fun dismissAddChoreDialog() {
        _uiState.update { it.copy(showAddChoreDialog = false) }
    }

    fun showReassignChoreDialog(chore: Chore) {
        _uiState.update { it.copy(showReassignChoreDialog = true, choreToReassign = chore) }
    }

    fun dismissReassignChoreDialog() {
        _uiState.update { it.copy(showReassignChoreDialog = false, choreToReassign = null) }
    }
}