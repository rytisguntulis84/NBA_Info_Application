package com.tglab.nbainfo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tglab.nbainfo.R
import com.tglab.nbainfo.data.model.Player
import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.ui.rows.PlayerRow
import com.tglab.nbainfo.ui.theme.NBAInfoTheme
import com.tglab.nbainfo.ui.theme.NbaLightBg
import com.tglab.nbainfo.ui.viewmodel.PlayersUiState
import com.tglab.nbainfo.ui.viewmodel.PlayersViewModel

@Composable
fun PlayersScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayersViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTeam by viewModel.selectedTeam.collectAsState()

    selectedTeam?.let { team ->
        TeamGamesBottomSheet(
            team = team,
            onDismiss = { viewModel.selectTeam(null) },
            backLabel = stringResource(R.string.back_to_players)
        )
    }

    PlayersScreenContent(
        modifier = modifier,
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSelectTeam = viewModel::selectTeam,
        onRetry = viewModel::retry
    )
}

@Composable
private fun PlayersScreenContent(
    modifier: Modifier = Modifier,
    uiState: PlayersUiState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSelectTeam: (Team?) -> Unit,
    onRetry: () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.tab_players),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NbaLightBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_hint),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.column_first_name),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    text = stringResource(R.string.column_last_name),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    text = stringResource(R.string.column_team),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (val state = uiState) {
                    is PlayersUiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.loading_players),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    is PlayersUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(
                                    if (state.isRateLimited) {
                                        R.string.rate_limit_title
                                    } else {
                                        R.string.error_loading_players
                                    }
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(
                                    if (state.isRateLimited) {
                                        R.string.rate_limit_message
                                    } else {
                                        R.string.error_try_again
                                    }
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text(stringResource(R.string.action_retry))
                            }
                        }
                    }

                    is PlayersUiState.Success -> {
                        if (state.players.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isBlank()) {
                                        stringResource(R.string.empty_players)
                                    } else {
                                        stringResource(
                                            R.string.empty_players_matching_query,
                                            searchQuery
                                        )
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                items(
                                    items = state.players,
                                    key = { it.id }
                                ) { player ->
                                    PlayerRow(
                                        player = player,
                                        onClick = {
                                            player.team?.let(onSelectTeam)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayersScreenPreview() {
    NBAInfoTheme {
        PlayersScreenContent(
            uiState = PlayersUiState.Success(
                players = listOf(
                    Player(
                        id = 228,
                        firstName = "Žydrūnas",
                        lastName = "Ilgauskas",
                        position = "C",
                        team = Team(
                            id = 6,
                            conference = "East",
                            division = "Central",
                            city = "Cleveland",
                            name = "Cavaliers",
                            fullName = "Cleveland Cavaliers",
                            abbreviation = "CLE"
                        )
                    )
                )
            ),
            searchQuery = "",
            onSearchQueryChanged = {},
            onSelectTeam = {},
            onRetry = {}
        )
    }
}
