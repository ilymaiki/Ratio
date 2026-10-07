package app.ratio.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.ratio.domain.Money

/**
 * Home / Este mes (neto.md §8): cifra grande + lista de etiquetas + [+].
 * Sin lógica: solo pinta el HomeUiState del ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, onAdd: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Este mes · ${state.month}") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Text("+") }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = Money.format(state.totalCents), fontSize = 40.sp)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.categories) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = row.name, modifier = Modifier.weight(1f))
                        Text(text = Money.format(row.totalCents))
                    }
                }
            }
        }
    }
}
