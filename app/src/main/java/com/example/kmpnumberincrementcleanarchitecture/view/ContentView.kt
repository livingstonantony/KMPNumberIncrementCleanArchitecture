package com.example.kmpnumberincrementcleanarchitecture.view


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmpnumberincrementcleanarchitecture.LocalAppContainer
import com.example.shared.viewmodel.CounterUiState
import com.example.shared.viewmodel.CounterViewModel

@Composable
fun ContentView(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel(
        factory = LocalAppContainer.current.mainViewModelFactory,
    )

) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row showing attempts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ATTEMPTS:",
                color = Color.Green
            )

            val attempts = when (uiState) {
                is CounterUiState.Success -> (uiState as CounterUiState.Success).count
                is CounterUiState.Error -> (uiState as CounterUiState.Error).count
                else -> 0
            }

            Text(text = "$attempts/10")
        }

        Spacer(modifier = Modifier.weight(1f))

        when (uiState) {
            is CounterUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Loading...")
                    LinearProgressIndicator(
                        modifier = Modifier.width(200.dp),
                        color = Color.Blue
                    )
                }
            }

            is CounterUiState.Success -> {
                val state = uiState as CounterUiState.Success
                Text(
                    text = "Count: ${state.count}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = LocalContentColor.current
                )
            }

            is CounterUiState.Error -> {
                val state = uiState as CounterUiState.Error
                Text(
                    text = "Count: ${state.count}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = LocalContentColor.current.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.message,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.reset() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reset", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.increment() },
            enabled = uiState !is CounterUiState.Loading &&
                    (uiState !is CounterUiState.Error || (uiState as CounterUiState.Error).canReset.not()),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState !is CounterUiState.Loading) Color.Blue else Color.Gray
            )
        ) {
            Text(
                "TAP TO INCREASE",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme { // Wrap in MaterialTheme for previews and proper styling
        ContentView()
    }
}
