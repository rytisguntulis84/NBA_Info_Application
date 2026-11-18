package com.tglab.nbainfo

import com.tglab.nbainfo.data.model.Player
import com.tglab.nbainfo.data.model.Team
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayersViewModelTest {

    @Test
    fun playerModel_correctPropertyValues() {
        val team = Team(6, "East", "Central", "Cleveland", "Cavaliers", "Cleveland Cavaliers", "CLE")
        val player = Player(
            id = 228,
            firstName = "Zydrunas",
            lastName = "Ilgauskas",
            position = "C",
            team = team
        )

        assertEquals("Zydrunas", player.firstName)
        assertEquals("Ilgauskas", player.lastName)
        assertEquals("Cleveland Cavaliers", player.team?.fullName)
    }
}
