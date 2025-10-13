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
