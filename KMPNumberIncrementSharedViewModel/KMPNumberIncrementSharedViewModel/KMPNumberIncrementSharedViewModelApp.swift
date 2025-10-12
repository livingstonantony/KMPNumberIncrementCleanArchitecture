//
//  KMPNumberIncrementSharedViewModelApp.swift
//  KMPNumberIncrementSharedViewModel
//
//  Created by Philosopher on 24/09/25.
//

import SwiftUI
import sharedKit

@main
struct KMPNumberIncrementSharedViewModelApp: App {

    let appContainer: ObservableValueWrapper<AppContainer>

    init() {
        self.appContainer = ObservableValueWrapper<AppContainer>(
            value: AppContainer()
        )
    }
    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
            .environmentObject(appContainer)
        }
    }
}
