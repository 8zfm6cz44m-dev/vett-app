import SwiftUI

/// Navigation payload: which substance to open, and whether the user arrived
/// via a search that mentioned overdose/emergency terms — if so, the detail
/// screen jumps straight to the overdose section instead of starting at the
/// top. This can matter in a literal life-or-death moment, so no scrolling
/// required to find it.
struct SubstanceLink: Hashable {
    let substance: Substance
    let highlightOverdose: Bool
}

private let overdoseSearchKeywords = ["overdose", "nødhjelp", "akutt", "død", "forgiftning"]

/// Coarse groupings for the "Fakta" list, inspired by rusopplysningen.no's
/// category taxonomy (a small, fixed set of umbrella categories).
///
/// Substances.json's own `category` field is deliberately specific/clinical
/// per substance (e.g. "Sentralstimulerende (røykbar fribase av kokain)" for
/// Crack, "Sentralstimulerende (katinon)" for Mefedron) — great for the
/// detail page, but grouping the list by that exact string would produce
/// ~20 near-duplicate, mostly-one-item sections. This buckets by keyword
/// instead, so variants fold together, and stays correct automatically as
/// substances are added later (the weekly fact-check job only edits
/// Substances.json, never this file).
enum SubstanceGroup: CaseIterable, Hashable {
    case sentralstimulerende
    case cannabinoider
    case dempende
    case opioider
    case psykedelika
    case dissosiative
    case reseptbelagt
    case nikotin
    case annet

    var title: String {
        switch self {
        case .sentralstimulerende: "Sentralstimulerende"
        case .cannabinoider: "Cannabinoider"
        case .dempende: "Dempende"
        case .opioider: "Opioider"
        case .psykedelika: "Psykedelika og hallusinogener"
        case .dissosiative: "Dissosiative stoffer"
        case .reseptbelagt: "Reseptbelagte legemidler"
        case .nikotin: "Nikotin og innåndingsmidler"
        case .annet: "Andre stoffer"
        }
    }

    var icon: String {
        switch self {
        case .sentralstimulerende: "bolt.fill"
        case .cannabinoider: "leaf.fill"
        case .dempende: "moon.fill"
        case .opioider: "cross.case.fill"
        case .psykedelika: "sparkles"
        case .dissosiative: "cloud.fill"
        case .reseptbelagt: "pills.fill"
        case .nikotin: "wind"
        case .annet: "questionmark.circle.fill"
        }
    }

    /// Buckets a substance by keyword-matching its raw `category` string,
    /// checked in priority order (most specific first) so e.g. an opioid
    /// that's also plant-based still lands under Opioider.
    ///
    /// One explicit override: Substances.json describes Cannabis clinically
    /// as "Dempende / svakt hallusinogent" (accurate for the detail page),
    /// but both rusopplysningen.no and everyday usage group it under
    /// Cannabinoider, so the list groups it there too.
    static func forSubstance(_ substance: Substance) -> SubstanceGroup {
        if substance.id == "cannabis" { return .cannabinoider }
        let c = substance.category.lowercased()
        if c.contains("opioid") { return .opioider }
        if c.contains("cannabinoider") { return .cannabinoider }
        if c.contains("psykedelika") { return .psykedelika }
        if c.contains("dissosiativ") { return .dissosiative }
        if c.contains("reseptbelagt legemiddel") { return .reseptbelagt }
        if c.contains("sentralstimulerende") { return .sentralstimulerende }
        if c.contains("dempende") { return .dempende }
        if c.contains("nikotinprodukt") || c.contains("innåndingsmiddel") { return .nikotin }
        return .annet
    }
}

struct SubstanceListView: View {
    /// When true, the search field is activated the moment this view
    /// appears — used by the dedicated "Søk" tab so the keyboard is ready
    /// immediately instead of requiring an extra tap.
    var autoFocusSearch: Bool = false
    var titleOverride: String? = nil

    @EnvironmentObject private var store: DataStore
    @State private var searchText = ""
    @State private var isSearchActive = false

    /// Splits the query into words and requires every word to appear
    /// somewhere in the substance's searchable text (order-independent),
    /// so e.g. "overdose kokain" finds Kokain even though that exact
    /// phrase never appears verbatim in the content.
    private var filtered: [Substance] {
        let words = searchText.lowercased().split(separator: " ").map(String.init)
        guard !words.isEmpty else { return store.substances }
        return store.substances.filter { substance in
            words.allSatisfy { wordMatches($0, in: substance) }
        }
    }

    /// `filtered`, bucketed into groups and ordered by `SubstanceGroup`'s
    /// case order — empty groups (e.g. no results for the current search)
    /// are dropped rather than shown as empty sections.
    private var groupedFiltered: [(group: SubstanceGroup, substances: [Substance])] {
        let buckets = Dictionary(grouping: filtered, by: SubstanceGroup.forSubstance)
        return SubstanceGroup.allCases.compactMap { group in
            guard let items = buckets[group], !items.isEmpty else { return nil }
            return (group, items)
        }
    }

    /// True if `query` appears in the substance's content, either as an exact
    /// substring anywhere in the full text (fast path, handles most
    /// searches), or — for queries of 3+ characters only — as a close
    /// typo-tolerant match against the substance's name/category/aliases.
    /// Delegates to `FuzzySearch.matches` so this uses exactly the same rules
    /// as `searchMentionsOverdose` below — see FuzzySearch.swift for why
    /// having two separate copies of this logic was itself the bug last
    /// time, and why it now lives in its own (unit-tested) file rather than
    /// as a private method on this view.
    private func wordMatches(_ query: String, in substance: Substance) -> Bool {
        FuzzySearch.matches(query, candidates: substance.fuzzyMatchWords, fullText: substance.searchableText)
    }

    var body: some View {
        List {
            ForEach(groupedFiltered, id: \.group) { entry in
                Section {
                    ForEach(entry.substances) { substance in
                        NavigationLink(value: SubstanceLink(substance: substance, highlightOverdose: searchMentionsOverdose)) {
                            SubstanceRow(substance: substance)
                        }
                    }
                } header: {
                    groupHeader(entry.group, count: entry.substances.count)
                }
            }

            Section {
            } footer: {
                Text("Innhold hentet fra rusinfo.no og rusopplysningen.no. Søk lagres aldri og sendes aldri noe sted.")
                    .font(.caption2)
            }
        }
        .listStyle(.insetGrouped)
        .navigationTitle(titleOverride ?? "Fakta om rusmidler")
        .searchable(text: $searchText, isPresented: $isSearchActive, prompt: "Søk etter stoff eller stikkord, f.eks. \"overdose kokain\"")
        .autocorrectionDisabled()
        .navigationDestination(for: SubstanceLink.self) { link in
            SubstanceDetailView(substance: link.substance, scrollToOverdose: link.highlightOverdose)
        }
        .onAppear {
            if autoFocusSearch { isSearchActive = true }
        }
    }

    /// True when the current search text includes — or fuzzily/partially
    /// matches — an overdose/emergency keyword, so opening a result jumps
    /// straight to the overdose section. Uses the exact same fuzzy-matching
    /// rules as the substance search itself (via `FuzzySearch.matches`), so a
    /// typo'd or truncated query like "ovr kwt" that finds Ketamin through
    /// fuzzy matching on "overdose" also triggers the auto-scroll — the two
    /// checks previously used different matching rules, which is what broke
    /// this last time.
    private var searchMentionsOverdose: Bool {
        let keywordsText = overdoseSearchKeywords.joined(separator: " ")
        let words = searchText.lowercased().split(separator: " ").map(String.init)
        return words.contains { word in
            FuzzySearch.matches(word, candidates: overdoseSearchKeywords, fullText: keywordsText)
        }
    }

    private func groupHeader(_ group: SubstanceGroup, count: Int) -> some View {
        HStack(spacing: 8) {
            Image(systemName: group.icon)
                .font(.system(size: 12, weight: .semibold))
                .foregroundStyle(.indigo)
                .frame(width: 22, height: 22)
                .background(Color.indigo.opacity(0.16))
                .clipShape(RoundedRectangle(cornerRadius: 7))
            Text(group.title)
                .font(.subheadline.weight(.bold))
                .foregroundStyle(.primary)
            Spacer()
            Text("\(count)")
                .font(.caption)
                .foregroundStyle(.secondary)
        }
        .padding(.vertical, 2)
        // insetGrouped List sections otherwise force header text into the
        // small-caps grey style — this header wants its own icon+title
        // treatment instead, matching the style AboutView/EmergencyView use
        // for their own custom headings.
        .textCase(nil)
    }
}

private struct SubstanceRow: View {
    let substance: Substance

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(substance.name)
                .font(.headline)
            Text(substance.shortDescription)
                .font(.subheadline)
                .foregroundStyle(.secondary)
                .lineLimit(2)
        }
        .padding(.vertical, 4)
        // Without this, VoiceOver treats the name and description as two
        // separate stops per row, making it slow to browse a list of
        // substances. One swipe per row now reads all of it. The specific
        // category (not just shown visually anymore now that rows are
        // grouped under a category section header) is still included here
        // so VoiceOver users don't lose that detail.
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(substance.name), \(substance.category). \(substance.shortDescription)")
    }
}

#Preview {
    NavigationStack {
        SubstanceListView()
            .environmentObject(DataStore())
    }
}
