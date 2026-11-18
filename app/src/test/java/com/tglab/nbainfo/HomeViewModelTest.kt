package com.tglab.nbainfo

import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.ui.viewmodel.SortOption
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun sortOptionEnum_hasCorrectStringResources() {
        assertEquals(R.string.sort_name, SortOption.NAME.labelRes)
        assertEquals(R.string.sort_city, SortOption.CITY.labelRes)
        assertEquals(R.string.sort_conference, SortOption.CONFERENCE.labelRes)
    }

    @Test
    fun teamSorting_byNameCityAndConference() {
        val teams = listOf(
            Team(1, "East", "Atlantic", "Boston", "Celtics", "Boston Celtics", "BOS"),
            Team(2, "West", "Pacific", "Los Angeles", "Lakers", "Los Angeles Lakers", "LAL"),
            Team(3, "East", "Southeast", "Atlanta", "Hawks", "Atlanta Hawks", "ATL")
        )

        val sortedByName = teams.sortedBy { (it.fullName ?: it.name ?: "").lowercase() }
        assertEquals("Atlanta Hawks", sortedByName[0].fullName)
        assertEquals("Boston Celtics", sortedByName[1].fullName)
        assertEquals("Los Angeles Lakers", sortedByName[2].fullName)

        val sortedByCity = teams.sortedBy { (it.city ?: "").lowercase() }
        assertEquals("Atlanta", sortedByCity[0].city)
        assertEquals("Boston", sortedByCity[1].city)
        assertEquals("Los Angeles", sortedByCity[2].city)

        val sortedByConference = teams.sortedBy { (it.conference ?: "").lowercase() }
        assertEquals("East", sortedByConference[0].conference)
        assertEquals("East", sortedByConference[1].conference)
        assertEquals("West", sortedByConference[2].conference)
    }
}
