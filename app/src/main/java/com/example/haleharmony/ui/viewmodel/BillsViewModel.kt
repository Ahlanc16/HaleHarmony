package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.haleharmony.HaleHarmonyApplication
import com.example.haleharmony.data.Bill
import com.example.haleharmony.data.BillsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BillsScreenUiState(
    val bills: List<Bill> = listOf()
)

class BillsViewModel(private val billsRepository: BillsRepository) : ViewModel() {

    val uiState: StateFlow<BillsScreenUiState> =
        billsRepository.getAllBillsStream().map { BillsScreenUiState(bills = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BillsScreenUiState()
            )

    fun addBill(name: String, amount: Double, dueDate: Long) {
        viewModelScope.launch {
            val newBill = Bill(
                name = name,
                amount = amount,
                dueDate = dueDate
            )
            billsRepository.insertBill(newBill)
        }
    }

    fun deleteBill(billId: String) {
        viewModelScope.launch {
            billsRepository.deleteBill(billId)
        }
    }

    fun toggleBillPaid(billId: String, isPaid: Boolean) {
        viewModelScope.launch {
            billsRepository.updateBillPaidStatus(billId, isPaid)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HaleHarmonyApplication)
                val billsRepository = application.container.billsRepository
                BillsViewModel(billsRepository = billsRepository)
            }
        }
    }
}
