package no.rusinnsikt.app.logic

import no.rusinnsikt.app.data.Substance

/**
 * Coarse groupings for the "Fakta" list — ported 1:1 from the iOS app's
 * SubstanceListView.swift (SubstanceGroup enum) so both apps group and
 * order substances identically.
 */
enum class SubstanceGroup(val title: String, val iconKey: String) {
    SENTRALSTIMULERENDE("Sentralstimulerende", "bolt"),
    CANNABINOIDER("Cannabinoider", "leaf"),
    DEMPENDE("Dempende", "moon"),
    OPIOIDER("Opioider", "medical"),
    PSYKEDELIKA("Psykedelika og hallusinogener", "sparkles"),
    DISSOSIATIVE("Dissosiative stoffer", "cloud"),
    RESEPTBELAGT("Reseptbelagte legemidler", "pill"),
    NIKOTIN("Nikotin", "wind"),
    ANNET("Andre stoffer", "question");

    companion object {
        /**
         * Buckets a substance by keyword-matching its raw `category` string,
         * checked in priority order (most specific first). One explicit
         * override: Cannabis is described clinically as "Dempende / svakt
         * hallusinogent" but is grouped under Cannabinoider everywhere else
         * (rusopplysningen.no, everyday usage) — same override as iOS.
         */
        fun forSubstance(substance: Substance): SubstanceGroup {
            if (substance.id == "cannabis") return CANNABINOIDER
            val c = substance.category.lowercase()
            return when {
                c.contains("opioid") -> OPIOIDER
                c.contains("cannabinoider") -> CANNABINOIDER
                c.contains("psykedelika") -> PSYKEDELIKA
                c.contains("dissosiativ") -> DISSOSIATIVE
                c.contains("reseptbelagt legemiddel") -> RESEPTBELAGT
                c.contains("sentralstimulerende") -> SENTRALSTIMULERENDE
                c.contains("dempende") -> DEMPENDE
                c.contains("nikotinprodukt") -> NIKOTIN
                // Poppers o.l. er innåndingsmidler uten nikotin — ikke samme hylle som snus.
                c.contains("innåndingsmiddel") -> ANNET
                else -> ANNET
            }
        }
    }
}
