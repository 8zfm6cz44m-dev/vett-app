import XCTest
@testable import Vett

/// Guards against a broken or malformed Substances.json ever reaching a
/// build undetected. DataStore.swift calls fatalError() if this file fails
/// to decode, which would crash the app at launch for every user — these
/// tests catch that at build/test time instead.
///
/// Requires this test target's "Host Application" to be set to Vett (the
/// default Xcode offers when creating a Unit Testing Bundle target), so
/// that Bundle.main resolves to the app bundle containing Substances.json —
/// exactly like DataStore.swift's own loading code.
final class SubstancesJSONTests: XCTestCase {

    private func loadDatabase() throws -> SubstanceDatabase {
        let url = try XCTUnwrap(
            Bundle.main.url(forResource: "Substances", withExtension: "json"),
            "Substances.json not found in the app bundle"
        )
        let data = try Data(contentsOf: url)
        return try JSONDecoder().decode(SubstanceDatabase.self, from: data)
    }

    func testSubstancesJSON_decodesWithoutError() throws {
        XCTAssertNoThrow(try loadDatabase())
    }

    func testSubstancesJSON_isNotEmpty() throws {
        let database = try loadDatabase()
        XCTAssertFalse(database.substances.isEmpty)
    }

    func testSubstancesJSON_allIDsAreUnique() throws {
        let database = try loadDatabase()
        let ids = database.substances.map(\.id)
        XCTAssertEqual(Set(ids).count, ids.count, "Duplicate substance id found — this can cause subtle SwiftUI list/navigation bugs")
    }

    func testSubstancesJSON_noSubstanceHasEmptyCoreFields() throws {
        let database = try loadDatabase()
        for substance in database.substances {
            XCTAssertFalse(substance.name.isEmpty, "Substance \(substance.id) has an empty name")
            XCTAssertFalse(substance.shortDescription.isEmpty, "Substance \(substance.id) has an empty shortDescription")
            XCTAssertFalse(substance.overdoseSigns.isEmpty, "Substance \(substance.id) has no overdoseSigns")
            XCTAssertFalse(substance.emergencyAction.isEmpty, "Substance \(substance.id) has an empty emergencyAction")
        }
    }

    func testEmergencyNumbers_areAllPresent() throws {
        let database = try loadDatabase()
        XCTAssertEqual(database.emergencyNumbers.all.count, 4)
        for number in database.emergencyNumbers.all {
            XCTAssertFalse(number.number.isEmpty, "\(number.label) has no phone number")
        }
    }

    func testDataStore_initializesWithoutCrashing() {
        // DataStore's init() force-loads and decodes Substances.json,
        // fatalError()-ing on failure. Constructing it here means a broken
        // JSON file fails this test instead of crashing the shipped app.
        let store = DataStore()
        XCTAssertFalse(store.substances.isEmpty)
    }
}
