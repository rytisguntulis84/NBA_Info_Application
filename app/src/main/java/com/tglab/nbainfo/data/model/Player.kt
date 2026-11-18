package com.tglab.nbainfo.data.model

import com.google.gson.annotations.SerializedName

data class PlayersResponse(
    @SerializedName("data")
    val data: List<Player>
)

data class Player(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("position")
    val position: String?,
    @SerializedName("team")
    val team: Team?
)
