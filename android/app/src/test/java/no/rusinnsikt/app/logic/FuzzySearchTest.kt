package no.rusinnsikt.app.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for the typo-tolerant search matching used by FaktaListScreen (via
 * FuzzySearch.kt) — this is the most complex, easiest-to-quietly-break logic
 * in the app, so it's worth pinning down with tests. Ported 1:1 from the iOS
 * app's VettTests/FuzzySearchTests.swift.
 */
class FuzzySearchTest {

    // MARK: - Levenshtein distance

    @Test
    fun levenshtein_identicalStrings_isZero() {
        assertEquals(0, FuzzySearch.levenshteinDistance("kokain", "kokain"))
    }

    @Test
    fun levenshtein_adjacentTransposition_countsAsOne() {
        // "ovre" vs "over" — a single adjacent-letter swap. Plain Levenshtein
        // would count this as 2 (substitute + substitute); this algorithm is
        // Damerau-Levenshtein specifically so a fast-typing swap counts as 1.
        assertEquals(1, FuzzySearch.levenshteinDistance("ovre", "over"))
    }

    @Test
    fun levenshtein_singleSubstitution_isOne() {
        assertEquals(1, FuzzySearch.levenshteinDistance("ketamin", "ketamon"))
    }

    @Test
    fun levenshtein_singleInsertionOrDeletion_isOne() {
        assertEquals(1, FuzzySearch.levenshteinDistance("kokain", "kokaiin"))
        assertEquals(1, FuzzySearch.levenshteinDistance("kokain", "okain"))
    }

    @Test
    fun levenshtein_emptyStrings() {
        assertEquals(0, FuzzySearch.levenshteinDistance("", ""))
        assertEquals(3, FuzzySearch.levenshteinDistance("", "abc"))
        assertEquals(3, FuzzySearch.levenshteinDistance("abc", ""))
    }

    // MARK: - FuzzySearch.matches

    @Test
    fun matches_exactSubstring_alwaysMatches() {
        assertTrue(FuzzySearch.matches("kokain", candidates = emptyList(), fullText = "fakta om kokain her"))
    }

    @Test
    fun matches_shortQuery_requiresExactSubstring() {
        // Queries under 3 characters never use fuzzy matching, only exact
        // substring — otherwise very short queries would match almost
        // everything.
        assertFalse(FuzzySearch.matches("kx", candidates = listOf("kokain"), fullText = "noe helt annet"))
        assertTrue(FuzzySearch.matches("ko", candidates = listOf("kokain"), fullText = "kokain er et stoff"))
    }

    @Test
    fun matches_typoOnCandidateWord_matchesWithinThreshold() {
        // "ovr" is a truncated/typo'd form of "overdose" — should still
        // match via the candidate list even though it's not a substring of
        // fullText.
        assertTrue(
            FuzzySearch.matches(
                "ovr",
                candidates = listOf("overdose", "nødhjelp"),
                fullText = "generell tekst uten stikkordet",
            ),
        )
    }

    @Test
    fun matches_tooDifferentWord_doesNotMatch() {
        assertFalse(
            FuzzySearch.matches(
                "banan",
                candidates = listOf("overdose", "nødhjelp"),
                fullText = "generell tekst uten stikkordet",
            ),
        )
    }

    @Test
    fun matches_prefixOfLongerCandidateWord() {
        // A truncated word should match against the start of a longer
        // candidate (e.g. typing "keta" while "ketamin" is the candidate).
        assertTrue(
            FuzzySearch.matches(
                "keta",
                candidates = listOf("ketamin"),
                fullText = "noe helt annet",
            ),
        )
    }
}
