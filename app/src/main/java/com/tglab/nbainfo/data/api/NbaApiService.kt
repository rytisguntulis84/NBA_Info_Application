package com.tglab.nbainfo.data.api

import com.tglab.nbainfo.data.model.GamesResponse
import com.tglab.nbainfo.data.model.PlayersResponse
import com.tglab.nbainfo.data.model.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NbaApiService {
    @GET("v1/teams")
    suspend fun getTeams(): TeamsResponse

    @GET("v1/games")
    suspend fun getGames(
        @Query("team_ids[]") teamId: Int,
        @Query("seasons[]") season: Int = 2025,
        @Query("per_page") perPage: Int = 25,
        @Query("cursor") cursor: Int? = null
    ): GamesResponse

    @GET("v1/players")
    suspend fun getPlayers(
        @Query("search") search: String? = null
    ): PlayersResponse
}
