package no.rusinnsikt.app.data

import no.rusinnsikt.app.logic.SubstanceGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Guards against a broken or malformed substances.json ever reaching a build
 * undetected. SubstanceRepository.load() has no fallback if this file fails
 * to parse, which would crash the app at launch for every user — these tests
 * catch that at build/test time instead. Ported from the iOS app's
 * VettTests/SubstancesJSONTests.swift.
 *
 * Runs against the real app/src/main/assets/substances.json on disk (via
 * parseSubstanceDatabase, the Context-independent parsing core that
 * SubstanceRepository.load() itself calls) rather than a duplicated test
 * fixture copy, so there is exactly one substances.json for these tests to
 * drift out of sync with.
 */
class SubstancesJsonTest {

    private fun loadDatabase(): SubstanceDatabase {
        // Gradle's unit test task runs with the module directory (app/) as
        // its working directory, but resolve a couple of fallbacks too so
        // this doesn't break if that ever changes.
        val candidates = listOf(
            File("src/main/assets/substances.json"),
            File("app/src/main/assets/substances.json"),
        )
        val file = candidates.firstOrNull { it.exists() }
            ?: error("substances.json not found (tried ${candidates.map { it.absolutePath }})")
        return parseSubstanceDatabase(file.readText(Charsets.UTF_8))
    }

    @Test
    fun substancesJson_decodesWithoutError() {
        loadDatabase()
    }

    @Test
    fun substancesJson_isNotEmpty() {
        assertFalse(loadDatabase().substances.isEmpty())
    }

    @Test
    fun substancesJson_allIdsAreUnique() {
        val ids = loadDatabase().substances.map { it.id }
        assertEquals(
            "Duplicate substance id found — this can cause subtle list/navigation bugs",
            ids.toSet().size,
            ids.size,
        )
    }

    @Test
    fun substancesJson_noSubstanceHasEmptyCoreFields() {
        for (substance in loadDatabase().substances) {
            assertFalse("Substance ${substance.id} has an empty name", substance.name.isEmpty())
            assertFalse("Substance ${substance.id} has an empty shortDescription", substance.shortDescription.isEmpty())
            assertFalse("Substance ${substance.id} has no overdoseSigns", substance.overdoseSigns.isEmpty())
            assertFalse("Substance ${substance.id} has an empty emergencyAction", substance.emergencyAction.isEmpty())
        }
    }

    @Test
    fun emergencyNumbers_areAllPresent() {
        val numbers = loadDatabase().emergencyNumbers
        assertEquals(4, numbers.all.size)
        assertEquals("113", numbers.ambulance.number)
        assertEquals("tel:113", numbers.ambulance.telUri)
        assertEquals("tel:22591300", numbers.poison.telUri)
        for (number in numbers.all) {
            assertFalse("${number.label} has no phone number", number.number.isEmpty())
            assertNotNull("${number.label} must produce a tel: URI", number.telUri)
        }
    }

    @Test
    fun substanceGroup_poppersIsNotNikotin() {
        val substances = loadDatabase().substances
        val poppers = substances.first { it.id == "poppers" }
        assertEquals(SubstanceGroup.ANNET, SubstanceGroup.forSubstance(poppers))
        val snus = substances.first { it.id == "snus" }
        assertEquals(SubstanceGroup.NIKOTIN, SubstanceGroup.forSubstance(snus))
        val vape = substances.first { it.id == "e-sigaretter" }
        assertEquals(SubstanceGroup.NIKOTIN, SubstanceGroup.forSubstance(vape))
        val cannabis = substances.first { it.id == "cannabis" }
        assertEquals(SubstanceGroup.CANNABINOIDER, SubstanceGroup.forSubstance(cannabis))
    }

    @Test
    fun substanceGroup_kratomXylazinNps() {
        val substances = loadDatabase().substances
        // Kratom's category string is "Plantebasert stoff (opioid- og
        // stimulerende virkning)" — contains "opioid", so it lands under
        // Opioider even though it's plant-based, per forSubstance's
        // keyword-priority order.
        val kratom = substances.first { it.id == "kratom" }
        assertEquals(SubstanceGroup.OPIOIDER, SubstanceGroup.forSubstance(kratom))
        // Xylazine and NPS don't match any of the specific keyword buckets
        // and fall through to Andre stoffer.
        val xylazin = substances.first { it.id == "xylazin" }
        assertEquals(SubstanceGroup.ANNET, SubstanceGroup.forSubstance(xylazin))
        val nps = substances.first { it.id == "nps" }
        assertEquals(SubstanceGroup.ANNET, SubstanceGroup.forSubstance(nps))
    }
}
