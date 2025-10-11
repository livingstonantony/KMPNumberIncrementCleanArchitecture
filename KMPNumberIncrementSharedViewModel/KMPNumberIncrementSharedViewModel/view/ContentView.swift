//
//  ContentView.swift
//  KMPNumberIncrementSharedViewModel
//
//  Created by Philosopher on 24/09/25.
//

import SwiftUI
import sharedKit

struct ContentView: View {
    
    @StateObject private var viewModel = IosViewModelStoreOwner()
    
    var body: some View {
        
        let viewModel: CounterViewModel = viewModel.viewModel(
            factory: CounterViewModelKt.mainViewModelFactory
        )
        
        VStack{
            
            HStack {
                Spacer() // Pushes the content to the right
                
                Text("ATTEMPTS:")
                    .foregroundColor(.green)
                    .padding(.top, 20) // Adds some padding from the top edge
                
                // Text at the top right
                Text("\(state.tapCount)/\(state.maxAttempts)")
                    .padding(.top, 20) // Adds some padding from the top edge
                    .padding(.trailing, 20) // Adds some padding from the right edge
            }
            
            Spacer()
            
            if state.status == .loading {
                VStack {
                    Text("Loading...")
                    ProgressView()
                        .progressViewStyle(LinearProgressViewStyle(tint: .blue))
                        .frame(width: 200)
                }
            }else{
                Text("Count: \(state.tapCount)")
                    .bold()
                    .font(.largeTitle)
                    .opacity(state.status == .error ? 0.3 : 1.0)
                
            }
            
            
            
            if state.status == .error  {
                VStack(spacing: 20) {
                    Text("Maximum attempts reached!")
                        .bold()
                        .foregroundColor(.red)
                    
                    Button("Reset") {
                        viewModel.processIntent(.reset)
                    }
                    .padding()
                    .background(Color.red)
                    .foregroundColor(.white)
                    .cornerRadius(10)
                }
                
            }
            Spacer()
            
            Button(action: {
                viewModel.processIntent(.increaseCount)
            }) {
                Text("TAP TO INCREASE")
                    .padding()
                    .background(state.status == .ready && state.tapCount < state.maxAttempts ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
            .disabled(state.tapCount >= state.maxAttempts || state.status == .loading)
            .padding(.bottom, 20)
        }
        
        Spacer() // Pushes the content to the center vertically
    }
    
}




#Preview {
    ContentView()
}
