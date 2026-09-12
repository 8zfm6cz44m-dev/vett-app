package no.rusinnsikt.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * First-launch screen — content ported 1:1 from the iOS app's
 * OnboardingView.swift, including the age/purpose framing App Review
 * teams expect for a neutral, non-promotional reference tool.
 */
@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // Unlike MainNavHost's screens, onboarding isn't wrapped in a
            // Scaffold (there's no bottom bar/topbar to show yet), so with
            // enableEdgeToEdge() it must claim its own inset padding or the
            // title/first paragraph/button can end up under the status bar
            // or gesture nav — the very first screen a reviewer or user sees.
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Book,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(44.dp).width(44.dp)
        )

        Text("Velkommen til Rusinnsikt", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        Text(
            "Rusinnsikt er et faktaoppslagsverk om rusmidler og risiko — på samme måte som en legemiddelkatalog. Appen er laget for å gi korrekt, nøytral informasjon, ikke for å oppfordre til bruk.",
            style = MaterialTheme.typography.bodyLarge
        )

        InfoRow(Icons.Filled.Verified, "Innholdet er basert på informasjon fra rusinfo.no (Oslo kommune) og rusopplysningen.no.")
        InfoRow(Icons.Filled.Warning, "Målet er å forebygge skader og redde liv — ikke å oppfordre til rusbruk.")
        InfoRow(Icons.Filled.WifiOff, "Fakta, søk og nødhjelp ligger i appen. Du trenger ikke internett for å bruke dem.")
        InfoRow(Icons.Filled.Lock, "Appen krever ingen konto, sender ingen data noe sted, og lagrer bare om du har sett denne skjermen. Alt skjer lokalt på telefonen din.")
        InfoRow(Icons.Filled.Person, "Innholdet omhandler rus og overdoserisiko og er beregnet for personer over 17 år.")

        Spacer(Modifier.height(4.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text("Jeg forstår, fortsett", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.width(24.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
