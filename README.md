<img src="https://github.com/livingstonantony/KMPNumberIncrementCleanArchitecture/blob/main/doc/flow.jpg" width="400"/> 
<img src="https://github.com/livingstonantony/KMPNumberIncrementCleanArchitecture/blob/main/doc/completed.gif" width="400"/>


## shared
[CounterViewModel.kt](https://github.com/livingstonantony/KMPNumberIncrementCleanArchitecture/blob/main/shared/src/commonMain/kotlin/com/example/shared/viewmodel/CounterViewModel.kt)

```
package com.example.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.CounterRepository

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CounterUiState {
    data class Success(val count: Int) : CounterUiState()
    object Loading : CounterUiState()
    data class Error(val count: Int, val message: String, val canReset: Boolean) : CounterUiState()
}
class CounterViewModel(
    private val repository: CounterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CounterUiState>(CounterUiState.Success(0))
    val uiState: StateFlow<CounterUiState> = _uiState

    private var currentCount = 0

    fun increment() {
        viewModelScope.launch {
            _uiState.value = CounterUiState.Loading
            val result = repository.increment(currentCount)
            _uiState.value = result.fold(
                onSuccess = {
                    currentCount = it
                    CounterUiState.Success(it)
                },
                onFailure = {
                    CounterUiState.Error(currentCount, it.message ?: "Unknown error", canReset = true)
                }
            )
        }
    }

    fun reset() {
        viewModelScope.launch {
            _uiState.value = CounterUiState.Loading
            currentCount = repository.reset()
            _uiState.value = CounterUiState.Success(currentCount)
        }
    }
}
```

## android

[ContentView.kt](https://github.com/livingstonantony/KMPNumberIncrementCleanArchitecture/blob/main/app/src/main/java/com/example/kmpnumberincrementcleanarchitecture/view/ContentView.kt)

```
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
        factory =  LocalAppContainer.current.mainViewModelFactory,
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
```

## iOS

[ContentView.swift](https://github.com/livingstonantony/KMPNumberIncrementCleanArchitecture/blob/main/KMPNumberIncrementSharedViewModel/KMPNumberIncrementSharedViewModel/view/ContentView.swift)

```
//
//  ContentView.swift
//  KMPNumberIncrementSharedViewModel
//
//  Created by Philosopher on 24/09/25.
//

import SwiftUI
import sharedKit
import Foundation



struct ContentView: View {
    /// Injects the `IOSViewModelStoreOwner` from the environment, which manages ViewModel lifecycle.
    @EnvironmentObject var viewModelStoreOwner: IosViewModelStoreOwner
    
    /// Injects the `AppContainer` from the environment, providing app-wide dependencies.
    @EnvironmentObject var appContainer: ObservableValueWrapper<AppContainer>
    
    var body: some View {
        let viewModel: CounterViewModel = viewModelStoreOwner.viewModel(
            factory: appContainer.value.mainViewModelFactory
        )
        
        Observing(viewModel.uiState) { counterUiState in
            content(for: counterUiState, viewModel: viewModel)
        }
        
    }
    
    
    @ViewBuilder
    private func content(for uiState: CounterUiState, viewModel: CounterViewModel) -> some View {
        let currentCount = attempts(from: uiState)
        let maxAttempts: Int32 = 10
        
        VStack {
            // --- Header / Attempts ---
            HStack {
                Spacer()
                Text("ATTEMPTS:")
                    .foregroundColor(.green)
                    .padding(.top, 20)
                
                Text("\(currentCount)/\(maxAttempts)")
                    .padding(.top, 20)
                    .padding(.trailing, 20)
            }
            
            Spacer()
            
            // --- Loading ---
            if uiState is CounterUiState.Loading {
                VStack {
                    Text("Loading...")
                    ProgressView()
                        .progressViewStyle(LinearProgressViewStyle(tint: .blue))
                        .frame(width: 200)
                }
            }
            // --- Success ---
            if let success = uiState as? CounterUiState.Success {
                Text("Count: \(success.count)")
                    .bold()
                    .font(.largeTitle)
            }
            // --- Error ---
            if let error = uiState as? CounterUiState.Error {
                VStack(spacing: 20) {
                    
                    Text("Count: \(error.count)")
                        .bold()
                        .font(.largeTitle)
                        .opacity(0.3)
                        .padding(.bottom, 14)
                    
                    Text("Maximum attempts reached!")
                        .bold()
                        .foregroundColor(.red)
                    
                    if error.canReset {
                        Button("Reset") {
                            viewModel.reset()
                        }
                        .padding()
                        .background(Color.red)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                    }
                }
            }
            
            
            Spacer()
            
            // --- Action Button ---
            Button(action: {
                viewModel.increment()
            }) {
                Text("TAP TO INCREASE")
                    .padding()
                    .background(buttonColor(for: uiState))
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
            .disabled(isButtonDisabled(for: uiState))
            .padding(.bottom, 20)
        }
    }

    
    private func attempts(from uiState: CounterUiState) -> Int32 {
        switch uiState {
        case let success as CounterUiState.Success:
            return success.count
        case let error as CounterUiState.Error:
            return error.count
        default:
            return 0
        }
    }
    
    private func buttonColor(for uiState: CounterUiState) -> Color {
        switch uiState {
        case is CounterUiState.Success:
            return .blue
        case is CounterUiState.Loading:
            return .gray
        case let error as CounterUiState.Error:
            return error.canReset ? .gray : .red
        default:
            return .blue
        }
    }
    
    private func isButtonDisabled(for uiState: CounterUiState) -> Bool {
        
        switch uiState {
        case is CounterUiState.Success:
            return false
        case is CounterUiState.Loading:
            return true
        case is CounterUiState.Error:
            return true
        default:
            return false
        }
    }
}





#Preview {
    ContentView()
}
```
