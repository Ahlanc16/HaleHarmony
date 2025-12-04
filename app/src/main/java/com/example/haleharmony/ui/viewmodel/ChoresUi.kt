package com.example.haleharmony.ui.viewmodel

import com.example.haleharmony.data.Chore
import java.util.UUID

data class ChoresUiState(
    val chores: List<Chore> = emptyList(),
    val showAddChoreDialog: Boolean = false,
    val showReassignChoreDialog: Boolean = false,
    val choreToReassign: Chore? = null
)