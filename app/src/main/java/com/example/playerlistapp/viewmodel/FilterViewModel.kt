package com.example.playerlistapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playerlistapp.data.prefs.FilterPrefs
import com.example.playerlistapp.data.prefs.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val prefs: FilterPrefs
) : ViewModel() {

    private val _state = MutableStateFlow(FilterState())
    val state: StateFlow<FilterState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.stateFlow.collectLatest { saved ->
                _state.value = saved
            }
        }
    }

    fun updateQuery(query: String) {
        val updated = _state.value.copy(query = query)
        _state.value = updated
        save(updated)
    }

    fun updateMinRating(rating: Int) {
        val updated = _state.value.copy(minRating = rating)
        _state.value = updated
        save(updated)
    }

    fun updateGenre(genre: String) {
        val updated = _state.value.copy(genre = genre)
        _state.value = updated
        save(updated)
    }

    private fun save(state: FilterState) {
        viewModelScope.launch {
            prefs.save(state)
        }
    }

    fun reset() {
        val default = FilterState()
        _state.value = default
        save(default)
    }
}