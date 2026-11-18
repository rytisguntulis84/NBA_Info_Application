package com.tglab.nbainfo.data.repository

import com.tglab.nbainfo.data.api.NbaApiService
import com.tglab.nbainfo.data.api.RetrofitClient
import com.tglab.nbainfo.data.model.GamesResponse
import com.tglab.nbainfo.data.model.Player
import com.tglab.nbainfo.data.model.Team

class NbaRepository(
    private val apiService: NbaApiService = RetrofitClient.apiService
) {
    suspend fun getTeams(): List<Team> {
        val response = apiService.getTeams()
        return response.data
    }

    suspend fun getGames(
        teamId: Int,
        season: Int = 2025,
        perPage: Int = 25,
        cursor: Int? = null
    ): GamesResponse {
        return apiService.getGames(
            teamId = teamId,
            season = season,
            perPage = perPage,
            cursor = cursor
        )
    }

    suspend fun getPlayers(search: String? = null): List<Player> {
        val response = apiService.getPlayers(search = if (search.isNullOrBlank()) null else search)
        return response.data
    }
}
