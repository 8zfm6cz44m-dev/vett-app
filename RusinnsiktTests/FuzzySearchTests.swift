import XCTest
@testable import Rusinnsikt

/// Tests for the typo-tolerant search matching used by SubstanceListView
/// (via FuzzySearch.swift) — this is the most complex, easiest-to-quietly-
/// break logic in the app, so it's worth pinning down with tests.
final class FuzzySearchTests: XCTestCase {

    // MARK: - Levenshtein distance (String extension in Models.swift)

    func testLevenshtein_identicalStrings_isZero() {
        XCTAssertEqual("kokain".levenshteinDistance(to: "kokain"), 0)
    }

    func testLevenshtein_adjacentTransposition_countsAsOne() {
        // "ovre" vs "over" — a single adjacent-letter swap. Plain Levenshtein
        // would count this as 2 (substitute + substitute); this algorithm is
        // Damerau-Levenshtein specifically so a fast-typing swap counts as 1.
        XCTAssertEqual("ovre".levenshteinDistance(to: "over"), 1)
    }

    func testLevenshtein_singleSubstitution_isOne() {
        XCTAssertEqual("ketamin".levenshteinDistance(to: "ketamon"), 1)
    }

    func testLevenshtein_singleInsertionOrDeletion_isOne() {
        XCTAssertEqual("kokain".levenshteinDistance(to: "kokaiin"), 1)
        XCTAssertEqual("kokain".levenshteinDistance(to: "okain"), 1)
    }

    func testLevenshtein_emptyStrings() {
        XCTAssertEqual("".levenshteinDistance(to: ""), 0)
        XCTAssertEqual("".levenshteinDistance(to: "abc"), 3)
        XCTAssertEqual("abc".levenshteinDistance(to: ""), 3)
    }

    // MARK: - FuzzySearch.matches

    func testMatches_exactSubstring_alwaysMatches() {
        XCTAssertTrue(FuzzySearch.matches("kokain", candidates: [], fullText: "fakta om kokain her"))
    }

    func testMatches_shortQuery_requiresExactSubstring() {
        // Queries under 3 characters never use fuzzy matching, only exact
        // substring — otherwise very short queries would match almost
        // everything.
        XCTAssertFalse(FuzzySearch.matches("kx", candidates: ["kokain"], fullText: "noe helt annet"))
        XCTAssertTrue(FuzzySearch.matches("ko", candidates: ["kokain"], fullText: "kokain er et stoff"))
    }

    func testMatches_typoOnCandidateWord_matchesWithinThreshold() {
        // "ovr" is a truncated/typo'd form of "overdose" — should still
        // match via the candidate list even though it's not a substring of
        // fullText.
        XCTAssertTrue(FuzzySearch.matches(
            "ovr",
            candidates: ["overdose", "nødhjelp"],
            fullText: "generell tekst uten stikkordet"
        ))
    }

    func testMatches_tooDifferentWord_doesNotMatch() {
        XCTAssertFalse(FuzzySearch.matches(
            "banan",
            candidates: ["overdose", "nødhjelp"],
            fullText: "generell tekst uten stikkordet"
        ))
    }

    func testMatches_prefixOfLongerCandidateWord() {
        // A truncated word should match against the start of a longer
        // candidate (e.g. typing "keta" while "ketamin" is the candidate).
        XCTAssertTrue(FuzzySearch.matches(
            "keta",
            candidates: ["ketamin"],
            fullText: "noe helt annet"
        ))
    }
}
