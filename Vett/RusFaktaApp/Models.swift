import Foundation

// MARK: - Data models matching Resources/Substances.json
// All content is bundled locally in the app. Nothing here is fetched from
// or sent to a network at runtime — see DataStore.swift.
//
// Explicitly `nonisolated`: the project's "Default Actor Isolation" build
// setting applies @MainActor to every declaration in the module unless it
// opts out. These are plain, synchronous data models decoded from JSON (and
// compared/hashed) on whatever thread/task happens to do that work — they
// were never meant to be actor-isolated, and leaving them implicitly
// MainActor-isolated is exactly what produced the Swift 6 warnings ("Main
// actor-isolated conformance of 'Substance' to 'Equatable'/'Decodable' cannot
// satisfy conformance requirement") noted in the code review.

nonisolated struct EmergencyNumber: Codable, Identifiable {
    var id: String { label }
    let label: String
    let number: String
}

nonisolated struct EmergencyNumbers: Codable {
    let ambulance: EmergencyNumber
    let poison: EmergencyNumber
    let rusinfo: EmergencyNumber
    let parorende: EmergencyNumber

    var all: [EmergencyNumber] { [ambulance, poison, rusinfo, parorende] }
}

nonisolated struct GeneralEmergencyGuidance: Codable {
    let title: String
    let intro: String
    let goldenRule: String
    let ifUnconscious: [String]
    let ifConsciousButHeavilySedated: [String]
    let ifAgitatedOrHallucinating: [String]
    let afterCare: String
    let mixingPrinciple: String
}

nonisolated struct Substance: Codable, Identifiable, Hashable {
    let id: String
    let name: String
    let aliases: [String]
    let category: String
    let riskLevel: String
    let shortDescription: String
    let effects: [String]
    let shortTermRisks: [String]
    let longTermRisks: [String]
    let overdoseSigns: [String]
    let emergencyAction: String
    let mixingRisks: String
    let legalStatus: String
    let sourceNote: String

    static func == (lhs: Substance, rhs: Substance) -> Bool { lhs.id == rhs.id }
    func hash(into hasher: inout Hasher) { hasher.combine(id) }

    /// Text used for local, on-device search matching only. Includes risk and
    /// overdose content so a search like "overdose kokain" finds Kokain even
    /// though that exact phrase never appears in the name/category/aliases.
    var searchableText: String {
        // "overdose" and "nødhjelp" are added explicitly: every substance page
        // has overdose/emergency guidance by definition, but the Norwegian
        // content text itself often doesn't literally contain those words
        // (e.g. Kokain's own text never spells out "overdose"), which would
        // otherwise make a search like "overdose kokain" fail to find it.
        ([name, category, riskLevel, shortDescription, "overdose", "nødhjelp"] + aliases + effects
            + shortTermRisks + longTermRisks + overdoseSigns + [emergencyAction, mixingRisks, legalStatus])
            .joined(separator: " ")
            .lowercased()
    }

    /// Individual lowercased words from searchableText, used for whole-phrase
    /// substring search only (see wordMatches in SubstanceListView).
    var searchableWords: [String] {
        searchableText
            .components(separatedBy: CharacterSet.alphanumerics.inverted)
            .filter { !$0.isEmpty }
    }

    /// A small, high-signal set of words used specifically for typo-tolerant
    /// (fuzzy) matching: the substance's name, category, aliases, and the two
    /// injected emergency keywords. Deliberately does NOT include the long
    /// free-text risk/effect paragraphs (hundreds of ordinary Norwegian
    /// words) — running fuzzy matching against that much text meant a short
    /// query like "am" or "ko" had, by sheer chance, a near-miss somewhere in
    /// almost every substance's text, so the results never narrowed as you
    /// typed. Restricting fuzzy matching to this smaller pool keeps typo
    /// tolerance for the things people actually search by (a substance's name
    /// or alias) without that noise.
    var fuzzyMatchWords: [String] {
        ([name, category, "overdose", "nødhjelp"] + aliases)
            .joined(separator: " ")
            .lowercased()
            .components(separatedBy: CharacterSet.alphanumerics.inverted)
            .filter { !$0.isEmpty }
    }
}

extension String {
    /// Edit-distance algorithm (optimal string alignment / Damerau-Levenshtein
    /// with adjacent transpositions) — the minimum number of single-character
    /// insertions, deletions, substitutions, OR swaps of two neighbouring
    /// letters needed to turn one string into the other.
    ///
    /// The transposition case matters a lot in practice: fast typing very
    /// often swaps two adjacent letters (e.g. "ovre" instead of "over"), and
    /// a plain Levenshtein distance counts that as two errors instead of one,
    /// which was causing real typos to fall outside the fuzzy-match
    /// threshold. This version counts it as a single error, matching how
    /// people actually mistype.
    func levenshteinDistance(to other: String) -> Int {
        let a = Array(self)
        let b = Array(other)
        let n = a.count
        let m = b.count
        if n == 0 { return m }
        if m == 0 { return n }

        var d = Array(repeating: Array(repeating: 0, count: m + 1), count: n + 1)
        for i in 0...n { d[i][0] = i }
        for j in 0...m { d[0][j] = j }

        for i in 1...n {
            for j in 1...m {
                let cost = a[i - 1] == b[j - 1] ? 0 : 1
                var best = min(d[i - 1][j] + 1, d[i][j - 1] + 1)
                best = min(best, d[i - 1][j - 1] + cost)
                if i > 1, j > 1, a[i - 1] == b[j - 2], a[i - 2] == b[j - 1] {
                    best = min(best, d[i - 2][j - 2] + 1)
                }
                d[i][j] = best
            }
        }
        return d[n][m]
    }
}

nonisolated struct SubstanceDatabase: Codable {
    let emergencyNumbers: EmergencyNumbers
    let generalEmergencyGuidance: GeneralEmergencyGuidance
    let substances: [Substance]
}
