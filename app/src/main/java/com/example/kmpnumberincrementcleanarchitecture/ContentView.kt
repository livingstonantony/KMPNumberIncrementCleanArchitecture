package com.example.kmpnumberincrementcleanarchitecture


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContentView(modifier: Modifier= Modifier) {
    var tapCount by remember { mutableStateOf(0) }
    val maxAttempts by remember { mutableStateOf(10) } // Assuming this is constant for now
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    // progress state is not directly used for display in your Swift code,
    // only the ProgressView visibility based on isLoading.

    // Coroutine scope for launching asynchronous tasks
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize() // Fills the entire available space
            .padding(bottom = 20.dp), // Padding at the very bottom of the Column
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 20.dp), // Padding similar to SwiftUI
            horizontalArrangement = Arrangement.End, // Pushes content to the right
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ATTEMPTS:",
                color = Color.Green // Material 3 uses Color objects directly
            )
            Text(
                text = "$tapCount/$maxAttempts"
            )
        }

        // Spacer to push content towards the center/bottom, similar to SwiftUI's Spacer behavior here
        Spacer(modifier = Modifier.weight(1f))

        if (isLoading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Loading...")
                LinearProgressIndicator( // Equivalent to LinearProgressViewStyle
                    modifier = Modifier.width(200.dp),
                    color = Color.Blue // Main progress color
                    // trackColor = Color.LightGray // Optional: background of the progress bar
                )
            }
        } else {
            Text(
                text = "Count: $tapCount",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp, // MaterialTheme.typography.headlineLarge (or similar for largeTitle)
                color = if (showError) LocalContentColor.current.copy(alpha = 0.3f) else LocalContentColor.current
            )
        }

        if (showError) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp) // Spacing between items
            ) {
                Text(
                    text = "Maximum attempts reached!",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Button(
                    onClick = {
                        tapCount = 0
                        showError = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reset", color = Color.White)
                }
            }
        }

        // Spacer to push the button towards the bottom
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (tapCount < maxAttempts) {
                    isLoading = true
                    coroutineScope.launch {
                        delay(1000L) // 1 second delay
                        tapCount += 1
                        isLoading = false
                        if (tapCount == maxAttempts) {
                            showError = true
                        }
                    }
                }
            },
            enabled = tapCount < maxAttempts && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (tapCount < maxAttempts && !isLoading) Color.Blue else Color.Gray
            )
        ) {
            Text(
                "TAP TO INCREASE",
                color = Color.White,
                modifier = Modifier.padding(8.dp) // Padding inside the button
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
