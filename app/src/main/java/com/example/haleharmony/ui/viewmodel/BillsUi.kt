package com.example.haleharmony.ui.viewmodel

import com.example.haleharmony.data.Bill

data class BillsUiState(
    val bills: List<Bill> = emptyList(),
    val showAddBillDialog: Boolean = false
)