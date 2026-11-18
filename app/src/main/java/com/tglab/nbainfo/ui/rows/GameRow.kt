package com.tglab.nbainfo.ui.rows

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tglab.nbainfo.R
import com.tglab.nbainfo.data.model.Game

@Composable
fun GameRow(
    game: Game,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = game.homeTeam?.fullName ?: game.homeTeam?.name ?: stringResource(R.string.value_unavailable),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(2.2f)
        )

        Text(
            text = game.homeTeamScore?.toString() ?: stringResource(R.string.value_unavailable),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.2f)
        )

        Text(
            text = game.visitorTeam?.fullName ?: game.visitorTeam?.name ?: stringResource(R.string.value_unavailable),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(2.2f)
        )

        Text(
            text = game.visitorTeamScore?.toString() ?: stringResource(R.string.value_unavailable),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.2f)
        )
    }
}
