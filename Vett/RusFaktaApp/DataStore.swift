import Foundation
import Combine

/// Loads the bundled Substances.json once at launch.
///
/// PRIVACY BY DESIGN:
/// - No network requests are ever made. All data ships inside the app bundle.
/// - No user accounts, no login, no iCloud sync, no analytics/tracking SDKs.
/// - The app stores nothing on-device beyond a single flag for whether
///   onboarding has been seen. Nothing leaves the phone, and nothing is
///   visible in a parent's/anyone else's browser or search history.
final class DataStore: ObservableObject {
    @Published private(set) var database: SubstanceDatabase

    init() {
        guard
            let url = Bundle.main.url(forResource: "Substances", withExtension: "json"),
            let data = try? Data(contentsOf: url),
            let decoded = try? JSONDecoder().decode(SubstanceDatabase.self, from: data)
        else {
            fatalError("Substances.json missing or malformed in app bundle.")
        }
        self.database = decoded
    }

    var substances: [Substance] {
        database.substances.sorted { $0.name.localizedCompare($1.name) == .orderedAscending }
    }

    var categories: [String] {
        Array(Set(database.substances.map(\.category))).sorted()
    }

    /// Wipes the one locally stored preference (onboarding flag). Nothing
    /// else is stored, so this fully resets the app's on-device footprint.
    func clearAllLocalData() {
        UserDefaults.standard.removeObject(forKey: OnboardingState.hasSeenOnboardingKey)
    }
}

enum OnboardingState {
    static let hasSeenOnboardingKey = "hasSeenOnboarding"
}
