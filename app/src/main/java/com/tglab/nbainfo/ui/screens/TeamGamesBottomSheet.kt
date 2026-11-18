package com.tglab.nbainfo.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tglab.nbainfo.R
import com.tglab.nbainfo.data.model.Game
import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.ui.rows.GameRow
import com.tglab.nbainfo.ui.theme.NBAInfoTheme
import com.tglab.nbainfo.ui.viewmodel.TeamGamesUiState
import com.tglab.nbainfo.ui.viewmodel.TeamGamesViewModel

@Composable
fun TeamGamesBottomSheet(
    team: Team,
    onDismiss: () -> Unit,
    backLabel: String,
    viewModel: TeamGamesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(team.id) {
        viewModel.loadGamesForTeam(team.id)
    }

    TeamGamesBottomSheetContent(
        team = team,
        onDismiss = onDismiss,
        backLabel = backLabel,
        uiState = uiState,
        onRetry = { viewModel.loadGamesForTeam(team.id) },
        onLoadMore = viewModel::loadMoreGames
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamGamesBottomSheetContent(
    team: Team,
    onDismiss: () -> Unit,
    backLabel: String,
    uiState: TeamGamesUiState,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {
        TeamGamesSheetBody(
            team = team,
            onDismiss = onDismiss,
            backLabel = backLabel,
            uiState = uiState,
            onRetry = onRetry,
            onLoadMore = onLoadMore
        )
    }
}

@Composable
private fun TeamGamesSheetBody(
    team: Team,
    onDismiss: () -> Unit,
    backLabel: String,
    uiState: TeamGamesUiState,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = backLabel,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            Text(
                text = team.fullName ?: team.name ?: stringResource(R.string.team_games_title),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(start = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.column_home_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(2.2f)
            )
            Text(
                text = stringResource(R.string.column_home_score),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.2f)
            )
            Text(
                text = stringResource(R.string.column_visitor_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(2.2f)
            )
            Text(
                text = stringResource(R.string.column_visitor_score),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.2f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val state = uiState) {
                is TeamGamesUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.loading_games),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                TeamGamesUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error_loading_games),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.error_try_again),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text(stringResource(R.string.action_retry))
                        }
                    }
                }

                is TeamGamesUiState.Success -> {
                    if (state.games.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.empty_games))
                        }
                    } else {
                        val listState = rememberLazyListState()

                        val shouldLoadMore by remember {
                            derivedStateOf {
                                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                                val totalItems = listState.layoutInfo.totalItemsCount
                                lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3
                            }
                        }

                        LaunchedEffect(shouldLoadMore) {
                            if (shouldLoadMore && !state.isLoadingMore && !state.isEndReached) {
                                onLoadMore()
                            }
                        }

                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(
                                items = state.games,
                                key = { it.id }
                            ) { game ->
                                GameRow(game = game)
                            }

                            if (state.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
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
private fun TeamGamesBottomSheetPreview() {
    val homeTeam = Team(
        id = 1,
        conference = "East",
        division = "Southeast",
        city = "Atlanta",
        name = "Hawks",
        fullName = "Atlanta Hawks",
        abbreviation = "ATL"
    )
    val visitorTeam = Team(
        id = 4,
        conference = "East",
        division = "Central",
        city = "Chicago",
        name = "Bulls",
        fullName = "Chicago Bulls",
        abbreviation = "CHI"
    )

    NBAInfoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            TeamGamesSheetBody(
                team = homeTeam,
                onDismiss = {},
                backLabel = stringResource(R.string.back_to_home),
                uiState = TeamGamesUiState.Success(
                    games = listOf(
                        Game(
                            id = 1,
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
                    ),
                    nextCursor = null,
                    isEndReached = true
                ),
                onRetry = {},
                onLoadMore = {}
            )
        }
    }
}
