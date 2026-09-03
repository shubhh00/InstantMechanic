package com.app.instantmechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Mechanic
import com.app.domain.repository.MechanicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MechanicUiState {
    data object Loading : MechanicUiState
    data class Success(val mechanics: List<Mechanic>) : MechanicUiState
    data class Error(val message: String) : MechanicUiState
}

@HiltViewModel
class MechanicViewModel @Inject constructor(
    private val repository: MechanicRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MechanicUiState>(MechanicUiState.Loading)

    val uiState: StateFlow<MechanicUiState> = _uiState

    init {
        loadMechanics()
    }

    fun loadMechanics() {
        viewModelScope.launch {
            _uiState.value = MechanicUiState.Loading

            try {
                val mechanics = repository.getMechanics()

                _uiState.value = MechanicUiState.Success(
                    mechanics.sortedBy { it.distanceKm }
                )

            } catch (e: Exception) {
                _uiState.value = MechanicUiState.Error(
                    "Unable to load mechanics. Please check your internet connection."
                )
            }
        }
    }

    fun getMechanicById(id: String): Mechanic? {
        val state = _uiState.value

        return if (state is MechanicUiState.Success) {
            state.mechanics.find { it.id == id }
        } else {
            null
        }
    }
}