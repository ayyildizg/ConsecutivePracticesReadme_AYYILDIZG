package com.example.playerlistapp.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.playerlistapp.viewmodel.PlayerViewModel
import com.example.playerlistapp.viewmodel.PlayerUiState
import com.example.playerlistapp.data.model.Player

@Composable
fun ListScreen(viewModel: PlayerViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is PlayerUiState.Loading -> LoadingScreen()
        is PlayerUiState.Error -> ErrorScreen((uiState as PlayerUiState.Error).message)
        is PlayerUiState.Success -> PlayerList((uiState as PlayerUiState.Success).players)
    }
}

@Composable
fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun PlayerList(players: List<Player>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(players) { player ->
            PlayerCard(player)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PlayerCard(player: Player) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = player.imageRes,
                contentDescription = player.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(player.name, style = MaterialTheme.typography.titleMedium)
                Text("№ ${player.number}")
                Text(player.position)
            }
        }
    }
}
