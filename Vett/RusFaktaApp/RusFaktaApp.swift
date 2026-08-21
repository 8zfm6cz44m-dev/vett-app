import SwiftUI

@main
struct RusFaktaApp: App {
    @StateObject private var store = DataStore()
    @AppStorage(OnboardingState.hasSeenOnboardingKey) private var hasSeenOnboarding = false

    var body: some Scene {
        WindowGroup {
            Group {
                if hasSeenOnboarding {
                    ContentView()
                        .environmentObject(store)
                } else {
                    OnboardingView(hasSeenOnboarding: $hasSeenOnboarding)
                        .environmentObject(store)
                }
            }
            // Never take an in-app screenshot/thumbnail-style approach that leaks
            // content to the multitasking app switcher preview by default beyond
            // what SwiftUI already does — kept intentionally plain/neutral.
        }
    }
}
