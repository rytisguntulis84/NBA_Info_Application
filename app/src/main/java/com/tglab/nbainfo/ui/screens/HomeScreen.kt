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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tglab.nbainfo.R
import com.tglab.nbainfo.data.model.Team
import com.tglab.nbainfo.ui.rows.TeamRow
import com.tglab.nbainfo.ui.theme.NBAInfoTheme
import com.tglab.nbainfo.ui.theme.NbaLightBg
import com.tglab.nbainfo.ui.viewmodel.HomeUiState
import com.tglab.nbainfo.ui.viewmodel.HomeViewModel
import com.tglab.nbainfo.ui.viewmodel.SortOption

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentSortOption by viewModel.sortOption.collectAsState()
    val selectedTeam by viewModel.selectedTeam.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        uiState = uiState,
        currentSortOption = currentSortOption,
        selectedTeam = selectedTeam,
        onSelectTeam = viewModel::selectTeam,
        onSortOptionSelected = viewModel::onSortOptionSelected,
        onRetry = viewModel::fetchTeams
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    currentSortOption: SortOption,
    selectedTeam: Team?,
    onSelectTeam: (Team?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onRetry: () -> Unit
) {
    val listState = rememberLazyListState()
    var showSortDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentSortOption) {
        listState.scrollToItem(0)
    }

    selectedTeam?.let { team ->
        TeamGamesBottomSheet(
            team = team,
            onDismiss = { onSelectTeam(null) },
            backLabel = stringResource(R.string.back_to_home)
        )
    }

    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.sort_by),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    SortOption.entries.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSortOptionSelected(option)
                                    showSortDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (option == currentSortOption),
                                onClick = {
                                    onSortOptionSelected(option)
                                    showSortDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(option.labelRes),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    Surface(
        color = Color.White,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tab_home),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )

                Surface(
                    onClick = { showSortDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    color = NbaLightBg,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(currentSortOption.labelRes),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
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
                    text = stringResource(R.string.column_team_name),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1.7f)
                )
                Text(
                    text = stringResource(R.string.column_city),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.column_conference),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
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
                when (uiState) {
                    is HomeUiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.loading_teams),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    HomeUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.error_loading_teams),
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

                    is HomeUiState.Success -> {
                        if (uiState.teams.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.empty_teams))
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                items(
                                    items = uiState.teams,
                                    key = { it.id }
                                ) { team ->
                                    TeamRow(
                                        team = team,
                                        onClick = { onSelectTeam(team) }
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
private fun HomeScreenPreview() {
    NBAInfoTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success(
                teams = listOf(
                    Team(
                        id = 1,
                        conference = "East",
                        division = "Southeast",
                        city = "Atlanta",
                        name = "Hawks",
                        fullName = "Atlanta Hawks",
                        abbreviation = "ATL"
                    )
                )
            ),
            currentSortOption = SortOption.NAME,
            selectedTeam = null,
            onSelectTeam = {},
            onSortOptionSelected = {},
            onRetry = {}
        )
    }
}
