import XCTest
@testable import Vett

/// Tests for Substance's derived search text/word lists (Models.swift).
final class SubstanceModelTests: XCTestCase {

    private func makeSubstance(
        id: String = "test-id",
        name: String = "Testkain",
        aliases: [String] = ["testis", "tk"],
        category: String = "Testkategori",
        riskLevel: String = "Middels",
        riskReduction: [String]? = nil
    ) -> Substance {
        Substance(
            id: id,
            name: name,
            aliases: aliases,
            category: category,
            riskLevel: riskLevel,
            shortDescription: "Kort beskrivelse.",
            effects: ["effekt en", "effekt to"],
            shortTermRisks: ["kortsiktig risiko"],
            longTermRisks: ["langsiktig risiko"],
            overdoseSigns: ["tegn en"],
            emergencyAction: "Ring 113.",
            mixingRisks: "Farlig med alkohol.",
            legalStatus: "Ulovlig.",
            sourceNote: "Kilde.",
            riskReduction: riskReduction
        )
    }

    func testRiskReductionIsSearchable() {
        let substance = makeSubstance(riskReduction: ["Bruk aldri alene."])
        XCTAssertTrue(substance.searchableText.contains("bruk aldri alene"))
    }

    func testSearchableText_includesNameAliasesAndInjectedKeywords() {
        let substance = makeSubstance()
        let text = substance.searchableText
        XCTAssertTrue(text.contains("testkain"))
        XCTAssertTrue(text.contains("testis"))
        // "overdose" and "nødhjelp" are always injected so overdose-related
        // searches find every substance even when the substance's own text
        // never literally says "overdose" — see the property's doc comment.
        XCTAssertTrue(text.contains("overdose"))
        XCTAssertTrue(text.contains("nødhjelp"))
    }

    func testSearchableText_isLowercased() {
        let substance = makeSubstance(name: "STORE Bokstaver")
        XCTAssertFalse(substance.searchableText.contains("STORE"))
        XCTAssertTrue(substance.searchableText.contains("store bokstaver"))
    }

    func testFuzzyMatchWords_excludesLongFreeTextFields() {
        // fuzzyMatchWords is deliberately a *small* pool (name/category/
        // aliases + the two keywords) — it must NOT include the long risk/
        // effect paragraphs, or short queries would fuzzy-match almost
        // everything by chance. See the property's doc comment for why.
        let substance = makeSubstance()
        XCTAssertFalse(substance.fuzzyMatchWords.contains("langsiktig"))
        XCTAssertTrue(substance.fuzzyMatchWords.contains("testkain"))
        XCTAssertTrue(substance.fuzzyMatchWords.contains("testis"))
    }

    func testTelURL_emptyNumberIsNil() {
        let empty = EmergencyNumber(label: "Tom", number: "")
        XCTAssertNil(empty.telURL)
    }

    func testEquatableAndHashable_useOnlyID() {
        let a = makeSubstance(id: "same-id", name: "Navn A")
        let b = makeSubstance(id: "same-id", name: "Navn B")
        XCTAssertEqual(a, b, "Substances with the same id should be equal even if other fields differ")
        XCTAssertEqual(a.hashValue, b.hashValue)
    }
}
