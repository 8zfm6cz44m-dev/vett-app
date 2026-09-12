package no.rusinnsikt.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import no.rusinnsikt.app.data.Substance
import no.rusinnsikt.app.data.SubstanceRepository
import no.rusinnsikt.app.logic.FuzzySearch
import no.rusinnsikt.app.logic.SubstanceGroup
import java.text.Collator
import java.util.Locale

private val overdoseSearchKeywords = listOf("overdose", "nødhjelp", "akutt", "død", "forgiftning")

fun iconFor(group: SubstanceGroup): ImageVector = when (group) {
    SubstanceGroup.SENTRALSTIMULERENDE -> Icons.Filled.Bolt
    SubstanceGroup.CANNABINOIDER -> Icons.Filled.Spa
    SubstanceGroup.DEMPENDE -> Icons.Filled.NightsStay
    SubstanceGroup.OPIOIDER -> Icons.Filled.LocalHospital
    SubstanceGroup.PSYKEDELIKA -> Icons.Filled.AutoAwesome
    SubstanceGroup.DISSOSIATIVE -> Icons.Filled.Cloud
    SubstanceGroup.RESEPTBELAGT -> Icons.Filled.Medication
    SubstanceGroup.NIKOTIN -> Icons.Filled.Air
    SubstanceGroup.ANNET -> Icons.Filled.QuestionMark
}

/**
 * Substance list — ported 1:1 from SubstanceListView.swift, including the
 * grouped sections, order-independent multi-word search, and typo-tolerant
 * fuzzy matching against name/category/aliases. Reused for the dedicated
 * "Søk" tab (autoFocusSearch = true) exactly like the iOS app reuses this
 * same view for its Søk tab.
 */
@Composable
fun FaktaListScreen(
    autoFocusSearch: Boolean = false,
    onOpenSubstance: (substanceId: String, highlightOverdose: Boolean) -> Unit
) {
    val context = LocalContext.current
    val database = SubstanceRepository.load(context)
    // Norwegian-locale collation so æ/ø/å sort correctly (after z, in that
    // order), same as the iOS list's default String comparison under the
    // nb-NO locale — a plain lowercase-string sort places them wrong.
    val collator = remember { Collator.getInstance(Locale("nb", "NO")) }
    val allSubstances = remember(database, collator) {
        database.substances.sortedWith(compareBy(collator) { it.name })
    }

    var query by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocusSearch) {
        if (autoFocusSearch) focusRequester.requestFocus()
    }

    fun wordMatches(word: String, substance: Substance): Boolean =
        FuzzySearch.matches(word, substance.fuzzyMatchWords, substance.searchableText)

    val words = query.lowercase().split(" ").filter { it.isNotEmpty() }
    val filtered = if (words.isEmpty()) allSubstances else allSubstances.filter { s -> words.all { wordMatches(it, s) } }

    val grouped = remember(filtered) {
        val buckets = filtered.groupBy { SubstanceGroup.forSubstance(it) }
        SubstanceGroup.entries.mapNotNull { g -> buckets[g]?.let { g to it } }
    }

    val keywordsText = overdoseSearchKeywords.joinToString(" ")
    val mentionsOverdose = words.any { w -> FuzzySearch.matches(w, overdoseSearchKeywords, keywordsText) }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .focusRequester(focusRequester),
            placeholder = { Text("Søk etter stoff eller stikkord, f.eks. \"overdose kokain\"") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false),
            colors = OutlinedTextFieldDefaults.colors()
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            grouped.forEach { (group, substances) ->
                item {
                    GroupHeader(group, substances.size)
                }
                items(substances, key = { it.id }) { substance ->
                    SubstanceRow(
                        substance = substance,
                        icon = iconFor(group),
                        onClick = { onOpenSubstance(substance.id, mentionsOverdose) }
                    )
                }
            }
            item {
                Text(
                    "Innhold hentet fra rusinfo.no og rusopplysningen.no. Søk lagres aldri og sendes aldri noe sted.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun GroupHeader(group: SubstanceGroup, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(22.dp)
                .width(22.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f), RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(iconFor(group), contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.height(13.dp).width(13.dp))
        }
        Text(group.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("$count", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SubstanceRow(substance: Substance, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(12.dp)
            // Without this, TalkBack treats the icon, name and description
            // as three separate stops per row — same fix as iOS VoiceOver's
            // .accessibilityElement(children: .ignore) on SubstanceRow.
            .semantics(mergeDescendants = true) {
                contentDescription = "${substance.name}, ${substance.category}. ${substance.shortDescription}"
                role = Role.Button
            },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(36.dp)
                .width(36.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.height(17.dp).width(17.dp))
        }
        Column {
            Text(substance.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(
                substance.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}
