package com.tglab.nbainfo.ui.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tglab.nbainfo.R
import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.data.repository.NbaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SortOption(@StringRes val labelRes: Int) {
    NAME(R.string.sort_name),
    CITY(R.string.sort_city),
    CONFERENCE(R.string.sort_conference)
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val teams: List<Team>) : HomeUiState
    data object Error : HomeUiState
}

class HomeViewModel(
    private val repository: NbaRepository = NbaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.NAME)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _selectedTeam = MutableStateFlow<Team?>(null)
    val selectedTeam: StateFlow<Team?> = _selectedTeam.asStateFlow()

    private var rawTeams: List<Team> = emptyList()

    init {
        fetchTeams()
    }

    fun selectTeam(team: Team?) {
        _selectedTeam.value = team
    }

    fun fetchTeams() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                rawTeams = repository.getTeams()
                _uiState.value = HomeUiState.Success(sortTeams(rawTeams, _sortOption.value))
            } catch (_: Exception) {
                _uiState.value = HomeUiState.Error
            }
        }
    }

    fun onSortOptionSelected(option: SortOption) {
        _sortOption.value = option
        if (_uiState.value is HomeUiState.Success) {
            _uiState.value = HomeUiState.Success(sortTeams(rawTeams, option))
        }
    }

    private fun sortTeams(teams: List<Team>, option: SortOption): List<Team> {
        return when (option) {
            SortOption.NAME -> teams.sortedBy { (it.fullName ?: it.name ?: "").lowercase() }
            SortOption.CITY -> teams.sortedBy { (it.city ?: "").lowercase() }
            SortOption.CONFERENCE -> teams.sortedBy { (it.conference ?: "").lowercase() }
        }
    }
}
