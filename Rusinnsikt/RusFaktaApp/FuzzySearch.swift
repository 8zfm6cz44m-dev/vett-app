import Foundation

/// Shared fuzzy/typo-tolerant text matching, used by both substance search
/// and overdose-keyword detection in SubstanceListView so the two can never
/// drift out of sync (an earlier version had two separate copies of this
/// logic, which is exactly what broke last time — see SubstanceListView.swift).
///
/// Pulled out into its own file, independent of any SwiftUI view, so it can
/// be unit tested directly — see RusinnsiktTests/FuzzySearchTests.swift.
enum FuzzySearch {
    /// True if `query` appears anywhere in `fullText` as an exact substring
    /// (fast path, handles most searches), or — for queries of 3+ characters
    /// only — as a close typo-tolerant match against one of `candidates`,
    /// either as a whole word or against the *start* of a longer candidate
    /// word (so a truncated or slightly mistyped word like "ovre" for
    /// "over[dose]" still matches).
    static func matches(_ query: String, candidates: [String], fullText: String) -> Bool {
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
}
