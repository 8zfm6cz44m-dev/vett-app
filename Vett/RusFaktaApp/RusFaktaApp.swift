import SwiftUI

@main
struct RusFaktaApp: App {
    @StateObject private var store = DataStore()
    @AppStorage(OnboardingState.hasSeenOnboardingKey) private var hasSeenOnboarding = false
    @Environment(\.scenePhase) private var scenePhase

    var body: some Scene {
        WindowGroup {
            ZStack {
                Group {
                    if hasSeenOnboarding {
                        ContentView()
                            .environmentObject(store)
                    } else {
                        OnboardingView(hasSeenOnboarding: $hasSeenOnboarding)
                            .environmentObject(store)
                    }
                }

                // PERSONVERN: iOS tar et øyeblikksbilde av skjermen idet appen
                // går i bakgrunnen, viser det i app-veksleren og lagrer det i
                // appens beholder. Uten dette dekket ville den sist åpnede
                // stoffsiden ligget synlig for alle som sveiper opp
                // app-veksleren på telefonen. Når appen ikke er aktiv, legges
                // derfor et nøytralt dekke over, slik at øyeblikksbildet bare
                // viser appnavn og ikon — samme prinsipp som bank-apper bruker.
                if scenePhase != .active {
                    PrivacyCover()
                }
            }
        }
    }
}

/// Nøytralt dekke som vises i app-veksleren i stedet for innholdet.
private struct PrivacyCover: View {
    var body: some View {
        ZStack {
            Color(.systemBackground)
                .ignoresSafeArea()
            VStack(spacing: 12) {
                Image(systemName: "book.closed.fill")
                    .font(.system(size: 44))
                    .foregroundStyle(.indigo)
                Text("Rusinnsikt")
                    .font(.title2.bold())
            }
        }
        .accessibilityHidden(true)
    }
}
