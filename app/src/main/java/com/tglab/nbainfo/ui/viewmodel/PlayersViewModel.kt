package com.tglab.nbainfo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tglab.nbainfo.data.model.Player
import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.data.repository.NbaRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.time.Duration.Companion.milliseconds

sealed interface PlayersUiState {
    data object Loading : PlayersUiState
    data class Success(val players: List<Player>) : PlayersUiState
    data class Error(val isRateLimited: Boolean = false) : PlayersUiState
}

@OptIn(FlowPreview::class)
class PlayersViewModel(
    private val repository: NbaRepository = NbaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayersUiState>(PlayersUiState.Loading)
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTeam = MutableStateFlow<Team?>(null)
    val selectedTeam: StateFlow<Team?> = _selectedTeam.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L.milliseconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    fetchPlayers(query)
                }
        }
    }

    fun selectTeam(team: Team?) {
        _selectedTeam.value = team
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun retry() {
        fetchPlayers(_searchQuery.value)
    }

    private fun fetchPlayers(query: String) {
        viewModelScope.launch {
            _uiState.value = PlayersUiState.Loading
            try {
                val players = repository.getPlayers(query)
                _uiState.value = PlayersUiState.Success(players)
            } catch (error: HttpException) {
                if (error.code() == 429) {
                    _uiState.value = PlayersUiState.Error(isRateLimited = true)
                } else {
                    _uiState.value = PlayersUiState.Error()
                }
            } catch (_: Exception) {
                _uiState.value = PlayersUiState.Error()
            }
        }
    }
}
