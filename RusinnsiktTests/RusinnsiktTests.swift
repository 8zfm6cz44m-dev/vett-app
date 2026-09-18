import XCTest
@testable import Rusinnsikt

/// Host-app-tester ligger i FuzzySearchTests, SubstanceModelTests og
/// SubstancesJSONTests. Denne fila er bevisst uten tom Swift Testing-stub.
final class RusinnsiktTests: XCTestCase {
    func testBundleDoesNotShipPlaceholderAppStoreID() {
        // App Store-lenker er fjernet så appen ikke trenger nett eller et
        // dummy-Apple-ID. Denne testen dokumenterer den beslutningen.
        XCTAssertNotNil(Bundle.main.bundleIdentifier)
    }
}
