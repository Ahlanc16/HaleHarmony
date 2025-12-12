/*package com.example.haleharmony.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.haleharmony.HaleHarmonyApplication
import com.example.haleharmony.data.AuthRepository
import com.example.haleharmony.data.HouseholdRepository
import com.example.haleharmony.data.HouseholdResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HouseholdViewModel(
    private val householdRepository: HouseholdRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _createJoinState = MutableStateFlow<CreateJoinState>(CreateJoinState.Idle)
    val createJoinState: StateFlow<CreateJoinState> = _createJoinState.asStateFlow()

    val householdResult: StateFlow<HouseholdResult> = householdRepository.getHousehold()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HouseholdResult.NotLoggedIn
        )

    fun createHousehold(name: String) {
        viewModelScope.launch {
            _createJoinState.value = CreateJoinState.Loading
            try {
                householdRepository.createHousehold(name)
                _createJoinState.value = CreateJoinState.Success
            } catch (e: Exception) {
                _createJoinState.value = CreateJoinState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun joinHousehold(householdId: String) {
        viewModelScope.launch {
            _createJoinState.value = CreateJoinState.Loading
            try {
                householdRepository.joinHousehold(householdId)
                _createJoinState.value = CreateJoinState.Success
            } catch (e: Exception) {
                _createJoinState.value = CreateJoinState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            authRepository.signIn(email, pass)
        }
    }

    fun createUser(email: String, pass: String) {
        viewModelScope.launch {
            authRepository.createUser(email, pass)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HaleHarmonyApplication)
                val householdRepository = application.container.householdRepository
                val authRepository = application.container.authRepository
                HouseholdViewModel(householdRepository = householdRepository, authRepository = authRepository)
            }
        }
    }
}

sealed interface CreateJoinState {
    object Idle : CreateJoinState
    object Loading : CreateJoinState
    object Success : CreateJoinState
    data class Error(val message: String) : CreateJoinState
}
*/