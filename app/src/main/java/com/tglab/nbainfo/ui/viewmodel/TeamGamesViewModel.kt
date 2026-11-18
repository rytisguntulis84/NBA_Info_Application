package com.tglab.nbainfo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tglab.nbainfo.data.model.Game
import com.tglab.nbainfo.data.repository.NbaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TeamGamesUiState {
    data object Loading : TeamGamesUiState
    data class Success(
        val games: List<Game>,
        val nextCursor: Int?,
        val isLoadingMore: Boolean = false,
        val isEndReached: Boolean = false
    ) : TeamGamesUiState
    data object Error : TeamGamesUiState
}

class TeamGamesViewModel(
    private val repository: NbaRepository = NbaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TeamGamesUiState>(TeamGamesUiState.Loading)
    val uiState: StateFlow<TeamGamesUiState> = _uiState.asStateFlow()

    private var currentTeamId: Int? = null

    fun loadGamesForTeam(teamId: Int) {
        if (currentTeamId == teamId && _uiState.value !is TeamGamesUiState.Error) {
            return
        }
        currentTeamId = teamId
        viewModelScope.launch {
            _uiState.value = TeamGamesUiState.Loading
            try {
                val response = repository.getGames(teamId = teamId, season = 2025, perPage = 25, cursor = null)
                val nextCursor = response.meta?.nextCursor
                _uiState.value = TeamGamesUiState.Success(
                    games = response.data,
                    nextCursor = nextCursor,
                    isEndReached = (nextCursor == null)
                )
            } catch (_: Exception) {
                _uiState.value = TeamGamesUiState.Error
            }
        }
    }

    fun loadMoreGames() {
        val currentState = _uiState.value as? TeamGamesUiState.Success ?: return
        val teamId = currentTeamId ?: return

        if (currentState.isLoadingMore || currentState.isEndReached || currentState.nextCursor == null) {
            return
        }

        _uiState.value = currentState.copy(isLoadingMore = true)

        viewModelScope.launch {
            try {
                val response = repository.getGames(
                    teamId = teamId,
                    season = 2025,
                    perPage = 25,
                    cursor = currentState.nextCursor
                )
                val nextCursor = response.meta?.nextCursor
                val updatedList = currentState.games + response.data
                _uiState.value = TeamGamesUiState.Success(
                    games = updatedList,
                    nextCursor = nextCursor,
                    isLoadingMore = false,
                    isEndReached = (nextCursor == null)
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(isLoadingMore = false)
            }
        }
    }
}
