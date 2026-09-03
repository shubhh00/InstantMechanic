package com.app.instantmechanic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Mechanic
import com.app.domain.repository.MechanicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MechanicViewModel @Inject constructor(
    private val repository: MechanicRepository
) : ViewModel() {

    private val _mechanics = MutableStateFlow<List<Mechanic>>(emptyList())
    val mechanics: StateFlow<List<Mechanic>> = _mechanics

    init {
        loadMechanics()
    }

    private fun loadMechanics() {
        viewModelScope.launch {
            try {
                val result = repository.getMechanics()
                _mechanics.value = result

                Log.d("MechanicViewModel", "Mechanics: $result")
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "Failed to load mechanics", e)
            }
        }
    }

    fun getMechanicById(id: String): Mechanic? {
        return mechanics.value.find { it.id == id }
    }
}