package no.rusinnsikt.app.logic

/**
 * Shared fuzzy/typo-tolerant text matching, used by both substance search and
 * overdose-keyword detection — ported 1:1 from the iOS app's FuzzySearch.swift
 * so search behaves identically on both platforms.
 */
object FuzzySearch {
    fun matches(query: String, candidates: List<String>, fullText: String): Boolean {
        if (query.length < 2) return fullText.contains(query)
        if (fullText.contains(query)) return true
        if (query.length < 3) return false

        val threshold = if (query.length <= 4) 1 else if (query.length <= 8) 2 else 3

        val wholeWordMatch = candidates.any { word ->
            kotlin.math.abs(word.length - query.length) <= threshold &&
                levenshteinDistance(word, query) <= threshold
        }
        if (wholeWordMatch) return true

        return candidates.any { word ->
            if (word.length <= query.length) return@any false
            val prefix = word.take(query.length)
            levenshteinDistance(prefix, query) <= threshold
        }
    }

    /**
     * Optimal string alignment distance (Damerau-Levenshtein with adjacent
     * transpositions) — counts a swap of two neighbouring letters (a very
     * common fast-typing typo, e.g. "ovre" for "over") as one error instead
     * of two, matching how people actually mistype.
     */
    fun levenshteinDistance(a: String, b: String): Int {
        val n = a.length
        val m = b.length
        if (n == 0) return m
        if (m == 0) return n

        val d = Array(n + 1) { IntArray(m + 1) }
        for (i in 0..n) d[i][0] = i
        for (j in 0..m) d[0][j] = j

        for (i in 1..n) {
            for (j in 1..m) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                var best = minOf(d[i - 1][j] + 1, d[i][j - 1] + 1)
                best = minOf(best, d[i - 1][j - 1] + cost)
                if (i > 1 && j > 1 && a[i - 1] == b[j - 2] && a[i - 2] == b[j - 1]) {
                    best = minOf(best, d[i - 2][j - 2] + 1)
                }
                d[i][j] = best
            }
        }
        return d[n][m]
    }
}
