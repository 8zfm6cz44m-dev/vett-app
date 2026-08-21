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

struct SubstanceListView: View {
    /// When true, the search field is activated the moment this view
    /// appears — used by the dedicated "Søk" tab so the keyboard is ready
    /// immediately instead of requiring an extra tap.
    var autoFocusSearch: Bool = false
    var titleOverride: String? = nil

    @EnvironmentObject private var store: DataStore
    @State private var searchText = ""
    @State private var selectedCategory: String? = nil
    @State private var isSearchActive = false

    /// Splits the query into words and requires every word to appear
    /// somewhere in the substance's searchable text (order-independent),
    /// so e.g. "overdose kokain" finds Kokain even though that exact
    /// phrase never appears verbatim in the content.
    private var filtered: [Substance] {
        let words = searchText.lowercased().split(separator: " ").map(String.init)
        return store.substances.filter { substance in
            let matchesCategory = selectedCategory == nil || substance.category == selectedCategory
            let matchesSearch = words.isEmpty || words.allSatisfy { wordMatches($0, in: substance) }
            return matchesCategory && matchesSearch
        }
    }

    /// True if `query` appears in the substance's content, either as an exact
    /// substring anywhere in the full text (fast path, handles most
    /// searches), or — for queries of 3+ characters only — as a close
    /// typo-tolerant match against the substance's name/category/aliases.
    /// Delegates to `fuzzyTextMatches` so this uses exactly the same rules as
    /// `searchMentionsOverdose` below — see that function's comment for why
    /// having two separate copies of this logic was itself the bug last time.
    private func wordMatches(_ query: String, in substance: Substance) -> Bool {
        fuzzyTextMatches(query, candidates: substance.fuzzyMatchWords, fullText: substance.searchableText)
    }

    /// Shared fuzzy/typo-tolerant matching: exact substring match anywhere in
    /// `fullText` (fast path), or — for queries of 3+ characters only — a
    /// close match against one of `candidates`, either as a whole word or
    /// against the *start* of a longer candidate word (so a truncated or
    /// slightly mistyped word like "ovre" for "over[dose]" still matches).
    /// This one function backs BOTH substance search and overdose-keyword
    /// detection, so the two can never drift out of sync again.
    private func fuzzyTextMatches(_ query: String, candidates: [String], fullText: String) -> Bool {
        guard query.count >= 2 else { return fullText.contains(query) }
        if fullText.contains(query) { return true }
        guard query.count >= 3 else { return false }

        let threshold = query.count <= 4 ? 1 : (query.count <= 8 ? 2 : 3)

        let wholeWordMatch = candidates.contains { word in
            abs(word.count - query.count) <= threshold && word.levenshteinDistance(to: query) <= threshold
        }
        if wholeWordMatch { return true }

        return candidates.contains { word in
            guard word.count > query.count else { return false }
            let prefix = String(word.prefix(query.count))
            return prefix.levenshteinDistance(to: query) <= threshold
        }
    }

    var body: some View {
        List {
            Section {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        categoryChip(nil, label: "Alle")
                        ForEach(store.categories, id: \.self) { category in
                            categoryChip(category, label: category)
                        }
                    }
                    .padding(.vertical, 4)
                }
                .listRowInsets(EdgeInsets())
                .listRowSeparator(.hidden)
                .padding(.horizontal)
            }

            Section {
                ForEach(filtered) { substance in
                    NavigationLink(value: SubstanceLink(substance: substance, highlightOverdose: searchMentionsOverdose)) {
                        SubstanceRow(substance: substance)
                    }
                }
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
    /// rules as the substance search itself (via `fuzzyTextMatches`), so a
    /// typo'd or truncated query like "ovr kwt" that finds Ketamin through
    /// fuzzy matching on "overdose" also triggers the auto-scroll — the two
    /// checks previously used different matching rules, which is what broke
    /// this last time.
    private var searchMentionsOverdose: Bool {
        let keywordsText = overdoseSearchKeywords.joined(separator: " ")
        let words = searchText.lowercased().split(separator: " ").map(String.init)
        return words.contains { word in
            fuzzyTextMatches(word, candidates: overdoseSearchKeywords, fullText: keywordsText)
        }
    }

    private func categoryChip(_ category: String?, label: String) -> some View {
        Button {
            selectedCategory = (selectedCategory == category) ? nil : category
        } label: {
            Text(label)
                .font(.caption.weight(.medium))
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(selectedCategory == category ? Color.indigo : Color(.secondarySystemBackground))
                .foregroundStyle(selectedCategory == category ? .white : .primary)
                .clipShape(Capsule())
        }
        .buttonStyle(.plain)
        .accessibilityAddTraits(selectedCategory == category ? .isSelected : [])
    }
}

private struct SubstanceRow: View {
    let substance: Substance

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(substance.name)
                .font(.headline)
            Text(substance.category)
                .font(.caption)
                .foregroundStyle(.secondary)
            Text(substance.shortDescription)
                .font(.subheadline)
                .foregroundStyle(.secondary)
                .lineLimit(2)
        }
        .padding(.vertical, 4)
        // Without this, VoiceOver treats the name, category, and description
        // as three separate stops per row, making it slow to browse a list
        // of substances. One swipe per row now reads all of it.
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
