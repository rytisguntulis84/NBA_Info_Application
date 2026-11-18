package com.tglab.nbainfo

import com.tglab.nbainfo.data.model.Game
import com.tglab.nbainfo.data.model.MetaInfo
import com.tglab.nbainfo.data.model.Team
import org.junit.Assert.assertEquals
import org.junit.Test

class TeamGamesViewModelTest {

    @Test
    fun gameModel_correctPropertyValues() {
        val homeTeam = Team(1, "East", "Southeast", "Atlanta", "Hawks", "Atlanta Hawks", "ATL")
        val visitorTeam = Team(4, "East", "Central", "Chicago", "Bulls", "Chicago Bulls", "CHI")

        val game = Game(
            id = 101,
            date = "2025-10-22",
            season = 2025,
            status = "Final",
            period = 4,
            time = "Final",
            postseason = false,
            homeTeamScore = 103,
            visitorTeamScore = 103,
            homeTeam = homeTeam,
            visitorTeam = visitorTeam
        )

        val meta = MetaInfo(nextCursor = 25, perPage = 25)

        assertEquals("Atlanta Hawks", game.homeTeam?.fullName)
        assertEquals(103, game.homeTeamScore)
        assertEquals("Chicago Bulls", game.visitorTeam?.fullName)
        assertEquals(103, game.visitorTeamScore)
        assertEquals(25, meta.nextCursor)
    }
}
