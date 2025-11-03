package com.example.playerlistapp.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playerlistapp.data.prefs.FilterPrefs
import com.example.playerlistapp.data.prefs.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val prefs: FilterPrefs
) : ViewModel() {

    val filterState = prefs.stateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FilterState()
    )

    fun updateFilter(newState: FilterState) {
        viewModelScope.launch {
            prefs.save(newState)
        }
    }

    fun resetFilters() {
        viewModelScope.launch {
            prefs.save(FilterState())
        }
    }
}

