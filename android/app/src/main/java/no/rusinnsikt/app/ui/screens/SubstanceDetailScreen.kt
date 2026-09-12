package no.rusinnsikt.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CallMerge
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.rusinnsikt.app.data.Substance
import no.rusinnsikt.app.util.safeStartActivity

/** Substance detail — ported 1:1 from SubstanceDetailView.swift. */
@Composable
fun SubstanceDetailScreen(substance: Substance, scrollToOverdose: Boolean, onNavigateToNodhjelp: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    // Actual on-screen Y offset (within the scrolling Column) of the
    // EmergencyCard, captured via onGloballyPositioned below — used instead
    // of a guessed percentage so the scroll lands exactly on the card,
    // regardless of how long the substance's other sections are.
    var emergencyCardOffset by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (substance.aliases.isNotEmpty()) {
                Text(
                    "Også kalt: ${substance.aliases.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabelRow(Icons.Filled.Sell, substance.category)
                LabelRow(Icons.Filled.Speed, substance.riskLevel)
            }
        }

        Card("Kort om stoffet", Icons.Filled.Info, substance.shortDescription)
        BulletCard("Virkning", Icons.Filled.AutoAwesome, substance.effects)
        BulletCard("Risiko på kort sikt", Icons.Filled.WarningAmber, substance.shortTermRisks)
        BulletCard("Risiko på lang sikt", Icons.Filled.HourglassEmpty, substance.longTermRisks)

        EmergencyCard(
            substance = substance,
            onNavigateToNodhjelp = onNavigateToNodhjelp,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                emergencyCardOffset = coordinates.positionInParent().y.toInt()
            }
        )

        Card("Fare ved blanding med andre stoffer", Icons.Filled.CallMerge, substance.mixingRisks)
        Card("Juridisk status i Norge", Icons.Filled.Balance, substance.legalStatus)

        Text(substance.sourceNote, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        ShareButton(substance)
    }

    LaunchedEffect(scrollToOverdose, emergencyCardOffset) {
        if (scrollToOverdose && emergencyCardOffset > 0) {
            // Small delay lets the initial layout/measure pass settle before
            // we animate, avoiding a jump-then-correct flicker.
            kotlinx.coroutines.delay(150)
            // Leave a little headroom above the card instead of pinning its
            // top edge to the very top of the viewport.
            val target = (emergencyCardOffset - 24).coerceIn(0, scrollState.maxValue)
            scrollState.animateScrollTo(target)
        }
    }
}

@Composable
private fun LabelRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(14.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Card(title: String, icon: ImageVector, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null)
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun BulletCard(title: String, icon: ImageVector, items: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null)
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items.forEach { BulletRow(it) }
    }
}

@Composable
private fun BulletRow(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("•")
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun EmergencyCard(substance: Substance, onNavigateToNodhjelp: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFD32F2F).copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.MonitorHeart, contentDescription = null, tint = Color(0xFFD32F2F))
            Text("Tegn på overdose", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
        }
        substance.overdoseSigns.forEach { BulletRow(it) }

        androidx.compose.material3.HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.LocalHospital, contentDescription = null, tint = Color(0xFFD32F2F))
            Text("Hva du skal gjøre", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD32F2F), RoundedCornerShape(12.dp))
                .border(3.dp, Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Er du i tvil? Ring 113 – uansett.", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                "Det er alltid riktig å ringe. Det er svært sjelden noen får problemer med politiet for å be om hjelp, og helsepersonell har lovpålagt taushetsplikt.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable {
                    safeStartActivity(
                        context,
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:113")),
                        "Fant ingen oppringingsapp. Ring 113 manuelt."
                    )
                }
            ) {
                Icon(Icons.Filled.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.width(18.dp))
                Text("Ring 113 nå", style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        Text(substance.emergencyAction, style = MaterialTheme.typography.bodyMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToNodhjelp)
                .semantics(mergeDescendants = true) {
                    contentDescription = "Se full nødhjelp-guide"
                }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Se full nødhjelp-guide",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFD32F2F)
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.width(18.dp)
            )
        }
    }
}

@Composable
private fun ShareButton(substance: Substance) {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText(substance))
            }
            safeStartActivity(
                context,
                Intent.createChooser(intent, null),
                "Fant ingen app å dele med."
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(Icons.Filled.Share, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text("Del info om ${substance.name}", fontWeight = FontWeight.SemiBold)
    }
}

/** Plain-text version of the entire substance page — ported 1:1 from iOS's fullPageShareText. */
private fun shareText(substance: Substance): String {
    val lines = mutableListOf(substance.name)
    if (substance.aliases.isNotEmpty()) lines.add("Også kalt: ${substance.aliases.joinToString(", ")}")
    lines.add("")
    lines.add(substance.shortDescription)
    lines.add("")
    lines.add("Virkning:")
    lines.addAll(substance.effects.map { "• $it" })
    lines.add("")
    lines.add("Risiko på kort sikt:")
    lines.addAll(substance.shortTermRisks.map { "• $it" })
    lines.add("")
    lines.add("Risiko på lang sikt:")
    lines.addAll(substance.longTermRisks.map { "• $it" })
    lines.add("")
    lines.add("Tegn på overdose:")
    lines.addAll(substance.overdoseSigns.map { "• $it" })
    lines.add("")
    lines.add("Hva du skal gjøre ved overdose:")
    lines.add(substance.emergencyAction)
    lines.add("")
    lines.add("Fare ved blanding med andre stoffer:")
    lines.add(substance.mixingRisks)
    lines.add("")
    lines.add("Juridisk status i Norge:")
    lines.add(substance.legalStatus)
    lines.add("")
    lines.add("Er du i tvil? Ring 113 – uansett.")
    lines.add("")
    lines.add("Delt fra Rusinnsikt-appen — gratis, nøytral rusinformasjon uten konto eller sporing. Fakta og nødhjelp ligger i appen.")
    return lines.joinToString("\n")
}
