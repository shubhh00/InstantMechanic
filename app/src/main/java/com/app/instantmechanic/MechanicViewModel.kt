package com.app.instantmechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Mechanic
import com.app.domain.model.isOpenNow
import com.app.domain.repository.MechanicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class MechanicListFilter {
    NEARBY,
    OPEN_NOW
}

data class MechanicUiState(
    val mechanics: List<Mechanic> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: MechanicListFilter = MechanicListFilter.NEARBY,
    val visibleMechanics: List<Mechanic> = emptyList(),
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class MechanicViewModel @Inject constructor(
    private val repository: MechanicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MechanicUiState())

    val uiState: StateFlow<MechanicUiState> = _uiState.asStateFlow()

    init {
        observeCachedMechanics()
        refreshMechanics()
    }

    private fun observeCachedMechanics() {
        viewModelScope.launch {
            repository.observeMechanics().collect { mechanics ->
                _uiState.update { currentState ->
                    currentState.copy(
                        mechanics = mechanics,
                        visibleMechanics = filterMechanics(
                            mechanics = mechanics,
                            query = currentState.searchQuery,
                            selectedFilter = currentState.selectedFilter
                        ),
                        isInitialLoading = if (mechanics.isNotEmpty()) {
                            false
                        } else {
                            currentState.isInitialLoading
                        }
                    )
                }
            }
        }
    }

    fun refreshMechanics() {
        viewModelScope.launch {
            val hasCachedData = _uiState.value.mechanics.isNotEmpty()

            _uiState.update {
                it.copy(
                    isInitialLoading = !hasCachedData,
                    isRefreshing = hasCachedData,
                    errorMessage = null
                )
            }

            try {
                repository.refreshMechanics()

                _uiState.update {
                    it.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        errorMessage = if (
                            currentState.mechanics.isEmpty()
                        ) {
                            "Unable to load mechanics. Check your internet connection."
                        } else {
                            "You're offline. Showing saved mechanics."
                        }
                    )
                }
            }
        }
    }

    fun getMechanicById(id: String): Mechanic? {
        return _uiState.value.mechanics.find { mechanic ->
            mechanic.id == id
        }
    }

    private fun filterMechanics(
        mechanics: List<Mechanic>,
        query: String,
        selectedFilter: MechanicListFilter
    ): List<Mechanic> {
        val normalizedQuery = query.trim()

        return mechanics
            .asSequence()
            .filter { mechanic ->
                normalizedQuery.isBlank() ||
                        mechanic.name.contains(
                            normalizedQuery,
                            ignoreCase = true
                        ) ||
                        mechanic.services.any { service ->
                            service.contains(
                                normalizedQuery,
                                ignoreCase = true
                            )
                        }
            }
            .filter { mechanic ->
                when (selectedFilter) {
                    MechanicListFilter.NEARBY -> true
                    MechanicListFilter.OPEN_NOW ->
                        mechanic.isOpenNow()
                }
            }
            .sortedBy { mechanic ->
                mechanic.distanceKm
            }
            .toList()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                visibleMechanics = filterMechanics(
                    mechanics = currentState.mechanics,
                    query = query,
                    selectedFilter = currentState.selectedFilter
                )
            )
        }
    }

    fun onFilterSelected(filter: MechanicListFilter) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                visibleMechanics = filterMechanics(
                    mechanics = currentState.mechanics,
                    query = currentState.searchQuery,
                    selectedFilter = filter
                )
            )
        }
    }

    fun clearSearch() {
        onSearchQueryChanged("")
    }

    fun showNearbyMechanics() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                selectedFilter = MechanicListFilter.NEARBY,
                visibleMechanics = filterMechanics(
                    mechanics = currentState.mechanics,
                    query = "",
                    selectedFilter =
                        MechanicListFilter.NEARBY
                )
            )
        }
    }
}