package com.tglab.nbainfo.data.model

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    @SerializedName("data")
    val data: List<Game>,
    @SerializedName("meta")
    val meta: MetaInfo?
)

data class MetaInfo(
    @SerializedName("next_cursor")
    val nextCursor: Int?,
    @SerializedName("per_page")
    val perPage: Int?
)

data class Game(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: String?,
    @SerializedName("season")
    val season: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("period")
    val period: Int?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("postseason")
    val postseason: Boolean?,
    @SerializedName("home_team_score")
    val homeTeamScore: Int?,
    @SerializedName("visitor_team_score")
    val visitorTeamScore: Int?,
    @SerializedName("home_team")
    val homeTeam: Team?,
    @SerializedName("visitor_team")
    val visitorTeam: Team?
)
