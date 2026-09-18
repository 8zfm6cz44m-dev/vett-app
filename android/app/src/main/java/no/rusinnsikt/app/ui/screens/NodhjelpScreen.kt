package no.rusinnsikt.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.rusinnsikt.app.data.EmergencyNumber
import no.rusinnsikt.app.data.GeneralRiskReduction
import no.rusinnsikt.app.data.SubstanceRepository
import no.rusinnsikt.app.util.safeStartActivity

/** Always-reachable emergency tab — ported 1:1 from EmergencyView.swift. */
@Composable
fun NodhjelpScreen() {
    val context = LocalContext.current
    val database = SubstanceRepository.load(context)
    val guidance = database.generalEmergencyGuidance
    val numbers = database.emergencyNumbers

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(guidance.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD32F2F), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Text(guidance.goldenRule, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CallButton(numbers.ambulance)
            CallButton(numbers.poison)
        }

        Text(guidance.intro, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        GuidanceSection("Hvis personen er bevisstløs", Icons.Filled.PersonOff, guidance.ifUnconscious, Color(0xFFD32F2F))
        GuidanceSection("Hvis personen er våken, men svært sløv", Icons.Filled.HelpOutline, guidance.ifConsciousButHeavilySedated, Color(0xFFEF6C00))
        GuidanceSection("Hvis personen er urolig eller hallusinerer", Icons.Filled.GraphicEq, guidance.ifAgitatedOrHallucinating, Color(0xFFEF6C00))

        InfoBlock("Etterpå", guidance.afterCare)
        InfoBlock("Om å blande stoffer", guidance.mixingPrinciple)

        RiskReductionSection(database.generalRiskReduction)

        AllNumbersSection(numbers.all)
    }
}

private fun dial(context: android.content.Context, telUri: String, displayNumber: String) {
    safeStartActivity(
        context,
        Intent(Intent.ACTION_DIAL, Uri.parse(telUri)),
        "Fant ingen oppringingsapp. Ring $displayNumber manuelt."
    )
}

@Composable
private fun CallButton(number: EmergencyNumber) {
    val context = LocalContext.current
    val uri = number.telUri
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD32F2F), RoundedCornerShape(14.dp))
            .then(
                if (uri != null) {
                    Modifier.clickable(onClickLabel = "Ring nå") { dial(context, uri, number.number) }
                } else Modifier
            )
            .padding(16.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "${number.label}, ${number.number}"
                if (uri != null) role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Phone, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(number.label, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
            Text(number.number, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GuidanceSection(title: String, icon: ImageVector, steps: List<String>, tint: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = tint)
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = tint)
        }
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "Steg ${index + 1}: $step"
                }
            ) {
                Column(
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .background(tint.copy(alpha = 0.15f), RoundedCornerShape(50)),
                ) {
                    Text(
                        "${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Text(step, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun InfoBlock(title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * «Hvis noen likevel skal bruke» — helsemyndighetenes generelle råd for
 * risikoreduksjon. Punktliste, ikke nummerert: rådene er ikke en rekkefølge.
 * Speiler riskReductionSection i EmergencyView.swift.
 */
@Composable
private fun RiskReductionSection(rr: GeneralRiskReduction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.Shield, contentDescription = null)
            Text(rr.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Text(rr.intro, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        rr.rules.forEach { rule ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("•")
                Text(rule, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
        Text(rr.sourceNote, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AllNumbersSection(numbers: List<EmergencyNumber>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Flere hjelpenumre", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        numbers.forEach { number ->
            val uri = number.telUri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (uri != null) {
                            Modifier.clickable(onClickLabel = "Ring nå") { dial(context, uri, number.number) }
                        } else Modifier
                    )
                    .semantics(mergeDescendants = true) {
                        contentDescription = "${number.label}, ${number.number}"
                        if (uri != null) role = Role.Button
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(number.label)
                Text(number.number, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
