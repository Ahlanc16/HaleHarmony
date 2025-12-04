package com.example.haleharmony.ui.viewmodel

import com.example.haleharmony.data.Visitor

data class VisitorsUiState(
    val visitors: List<Visitor> = emptyList(),
    val showAddVisitorDialog: Boolean = false
)