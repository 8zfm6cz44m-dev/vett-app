package no.rusinnsikt.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.rusinnsikt.app.data.EmergencyNumber
import no.rusinnsikt.app.data.OnboardingState
import no.rusinnsikt.app.data.SubstanceRepository
import no.rusinnsikt.app.util.safeStartActivity

private val shareAppText = """
    Rusinnsikt er et nøytralt oppslagsverk om rusmidler og risiko — gratis, uten konto og uten sporing. Fakta og nødhjelp ligger i appen.

    Ved mistanke om overdose: ring 113.
""".trimIndent()

/** About screen — ported 1:1 from AboutView.swift. */
@Composable
fun OmScreen(onPrivacyPolicy: () -> Unit, onSupport: () -> Unit, onOnboardingReset: () -> Unit) {
    val context = LocalContext.current
    val database = SubstanceRepository.load(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Om Rusinnsikt", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(
                "Rusinnsikt er et nøytralt oppslagsverk om rusmidler og risiko, laget for å gi ungdom og andre lettforståelig, faktabasert informasjon — ikke for å oppfordre til bruk. Målet er å forebygge skade og gjøre det enklere å søke hjelp raskt hvis noe går galt.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Section("Kilder") {
            LabelLine(Icons.Filled.AccountBalance, "rusinfo.no (Oslo kommune)")
            LabelLine(Icons.Filled.AccountBalance, "rusopplysningen.no")
            Text(
                "Innholdet i appen er skrevet om og forkortet fra disse kildene og ligger ferdig i appen. Appen åpner ikke nettstedene. For chat, stoffanalyse eller fullstendig oppdatert tekst må du selv slå opp kildene i en nettleser.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Section("Personvern") {
            LabelLine(Icons.Filled.PersonOff, "Ingen konto, ingen innlogging")
            LabelLine(Icons.Filled.WifiOff, "Fakta, søk og nødhjelp krever ikke internett")
            LabelLine(Icons.Filled.VisibilityOff, "Ingen analyse- eller sporingsverktøy")
            LabelLine(Icons.Filled.Lock, "Lokalt lagres kun om du har sett velkomstskjermen")
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onPrivacyPolicy),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Full personvernerklæring", color = MaterialTheme.colorScheme.primary)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSupport)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Støtt Rusinnsikt (valgfritt)")
            }
            Text(
                "Helt frivillig — appen er og forblir gratis. Ingen kjøp i appen.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Spre appen", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
                    .clickable {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareAppText)
                        }
                        safeStartActivity(context, Intent.createChooser(intent, null), "Fant ingen app å dele med.")
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Del appen med andre")
            }
            Text(
                "Deler vanlig tekst — ingen Play Store-lenke og ingen nettforespørsel.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Section("Hjelpenumre") {
            database.emergencyNumbers.all.forEach { number -> NumberRow(number) }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:elofsson.martin@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Tilbakemelding om Rusinnsikt-appen")
                        }
                        safeStartActivity(context, intent, "Fant ingen e-post-app. Send til elofsson.martin@gmail.com manuelt.")
                    }
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Gi tilbakemelding")
            }
            Text(
                "Åpner e-post-appen. Ingenting sendes før du selv trykker send.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = {
                OnboardingState.clearAll(context)
                onOnboardingReset()
            }) {
                Icon(Icons.Filled.RestartAlt, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                Text("Nullstill velkomstskjerm")
            }
            Text(
                "Sletter den ene lokale innstillingen. Velkomstskjermen vises neste gang du åpner appen.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Section("Ansvarsfraskrivelse") {
            Text(
                "Rusinnsikt gir generell, faktabasert informasjon og erstatter ikke profesjonell medisinsk vurdering, akutthjelp eller rådgivning. Innholdet er skrevet om og forkortet fra offentlige kilder og kan inneholde feil eller bli utdatert. Ved mistanke om overdose eller forgiftning: ring alltid 113 eller Giftinformasjonen, uavhengig av hva som står i appen. Rusinnsikt og appens utvikler er ikke ansvarlig for beslutninger tatt på grunnlag av innholdet.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            "Rusinnsikt er et uavhengig informasjonsprosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Section(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
private fun LabelLine(icon: ImageVector, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text)
    }
}

@Composable
private fun NumberRow(number: EmergencyNumber) {
    val context = LocalContext.current
    val uri = number.telUri
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (uri != null) {
                    Modifier.clickable(onClickLabel = "Ring nå") {
                        safeStartActivity(
                            context,
                            Intent(Intent.ACTION_DIAL, Uri.parse(uri)),
                            "Fant ingen oppringingsapp. Ring ${number.number} manuelt."
                        )
                    }
                } else Modifier
            )
            .semantics(mergeDescendants = true) {
                contentDescription = "${number.label}, ${number.number}"
                if (uri != null) role = Role.Button
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(number.label)
        Text(number.number, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
