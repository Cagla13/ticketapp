package com.turkcell.ticketapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.data.remote.EventApi
import com.turkcell.data.remote.EventDto
import com.turkcell.data.remote.TicketDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val events: List<EventDto>, val tickets: List<TicketDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(private val eventApi: EventApi) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val events = eventApi.getEvents()
                val tickets = eventApi.getTickets()
                _uiState.value = HomeUiState.Success(events, tickets)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Bir hata oluştu")
            }
        }
    }
}