package no.rusinnsikt.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for Substance's derived search text/word lists (data/Models.kt).
 * Ported from the iOS app's VettTests/SubstanceModelTests.swift.
 */
class SubstanceModelTest {

    private fun makeSubstance(
        id: String = "test-id",
        name: String = "Testkain",
        aliases: List<String> = listOf("testis", "tk"),
        category: String = "Testkategori",
        riskLevel: String = "Middels",
    ) = Substance(
        id = id,
        name = name,
        aliases = aliases,
        category = category,
        riskLevel = riskLevel,
        shortDescription = "Kort beskrivelse.",
        effects = listOf("effekt en", "effekt to"),
        shortTermRisks = listOf("kortsiktig risiko"),
        longTermRisks = listOf("langsiktig risiko"),
        overdoseSigns = listOf("tegn en"),
        emergencyAction = "Ring 113.",
        mixingRisks = "Farlig med alkohol.",
        legalStatus = "Ulovlig.",
        sourceNote = "Kilde.",
    )

    @Test
    fun searchableText_includesNameAliasesAndInjectedKeywords() {
        val substance = makeSubstance()
        val text = substance.searchableText
        assertTrue(text.contains("testkain"))
        assertTrue(text.contains("testis"))
        // "overdose" and "nødhjelp" are always injected so overdose-related
        // searches find every substance even when the substance's own text
        // never literally says "overdose" — see the property's doc comment.
        assertTrue(text.contains("overdose"))
        assertTrue(text.contains("nødhjelp"))
    }

    @Test
    fun searchableText_isLowercased() {
        val substance = makeSubstance(name = "STORE Bokstaver")
        assertFalse(substance.searchableText.contains("STORE"))
        assertTrue(substance.searchableText.contains("store bokstaver"))
    }

    @Test
    fun fuzzyMatchWords_excludesLongFreeTextFields() {
        // fuzzyMatchWords is deliberately a *small* pool (name/category/
        // aliases + the two keywords) — it must NOT include the long risk/
        // effect paragraphs, or short queries would fuzzy-match almost
        // everything by chance. See the property's doc comment for why.
        val substance = makeSubstance()
        assertFalse(substance.fuzzyMatchWords.contains("langsiktig"))
        assertTrue(substance.fuzzyMatchWords.contains("testkain"))
        assertTrue(substance.fuzzyMatchWords.contains("testis"))
    }

    @Test
    fun telUri_emptyNumberIsNull() {
        val empty = EmergencyNumber(label = "Tom", number = "")
        assertNull(empty.telUri)
    }

    @Test
    fun equatableAndHashable_useAllFields() {
        // Unlike iOS's Substance (which implements Equatable/Hashable using
        // only `id`), the Kotlin data class's generated equals()/hashCode()
        // compare every constructor property. Two substances that merely
        // share an id are therefore NOT equal here — this test documents
        // that intentional platform difference rather than assuming parity.
        val a = makeSubstance(id = "same-id", name = "Navn A")
        val b = makeSubstance(id = "same-id", name = "Navn B")
        assertFalse("Substances with the same id but different fields should not be equal", a == b)

        val c = makeSubstance(id = "same-id", name = "Navn A")
        assertEquals(a, c)
        assertEquals(a.hashCode(), c.hashCode())
    }
}
