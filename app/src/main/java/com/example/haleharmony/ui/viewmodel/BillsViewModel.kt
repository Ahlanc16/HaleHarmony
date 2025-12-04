package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.haleharmony.data.Bill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import java.util.UUID

class BillsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    init {
        // example data
        _uiState.value = BillsUiState(
            bills = listOf(
                Bill(name = "Rent", amount = 1200.00, dueDate = Date()),
                Bill(name = "Internet", amount = 60.00, dueDate = Date(), isPaid = true),
                Bill(name = "Electricity", amount = 75.50, dueDate = Date())
            )
        )
    }
    // adds bills
    fun addBill(name: String, amount: Double, dueDate: Date) {
        val newBill = Bill(name = name, amount = amount, dueDate = dueDate)
        _uiState.update {
            it.copy(bills = it.bills + newBill)
        }
    }
    // deletes bills
    fun deleteBill(billId: UUID) {
        _uiState.update { currentState ->
            val updatedBills = currentState.bills.filter { it.id != billId }
            currentState.copy(bills = updatedBills)
        }
    }
    //toggles if its paid or not
    fun toggleBillPaid(billId: UUID) {
        _uiState.update { currentState ->
            val updatedBills = currentState.bills.map {
                if (it.id == billId) {
                    it.copy(isPaid = !it.isPaid)
                } else {
                    it
                }
            }
            currentState.copy(bills = updatedBills)
        }
    }

    fun showAddBillDialog() {
        _uiState.update { it.copy(showAddBillDialog = true) }
    }

    fun dismissAddBillDialog() {
        _uiState.update { it.copy(showAddBillDialog = false) }
    }
}