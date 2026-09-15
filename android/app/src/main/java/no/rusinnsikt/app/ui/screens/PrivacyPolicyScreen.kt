package no.rusinnsikt.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Full personvernerklæring, portert 1:1 fra PrivacyPolicyView.swift. Ingen nettforespørsel. */
@Composable
fun PrivacyPolicyScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Personvernerklæring", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("Sist oppdatert 6. september 2026.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Text("Rusinnsikt er laget for å virke helt uten internett. All faktainformasjon, nødhjelp og søk ligger i appen. Vi samler ikke inn personopplysninger, har ingen konto og bruker ingen analyse- eller reklameverktøy.")

        Section("Hva appen lagrer", "Én lokal innstilling: om du har sett velkomstskjermen. Den ligger i telefonens vanlige innstillinger (SharedPreferences) og sendes aldri noe sted. Du kan slette den under Om → Nullstill velkomstskjerm.")
        Section("Hva appen ikke gjør", "Ingen innlogging, ingen sky, ingen sporing, ingen tredjeparts SDK-er for statistikk. Appen henter ikke innhold fra internett når du leser fakta eller nødhjelp.")
        Section("Telefon og e-post", "Nødnumre åpner telefon-dialeren (tel:). Tilbakemelding åpner e-post-appen (mailto:). Selve samtalen eller e-posten går via operatør eller e-postleverandør først når du sender eller ringer — det er utenfor Rusinnsikt.")
        Section("Kilder", "Teksten er forkortet fra offentlige kilder (rusinfo.no / Oslo kommune og rusopplysningen.no). Appen åpner ikke disse nettstedene. For fullstendig og oppdatert informasjon, chat eller stoffanalyse må du selv åpne kildene i en nettleser.")
        Section("Kontakt", "Spørsmål om personvern: post@rusinnsikt.no")

        Text(
            "Rusinnsikt er et uavhengig prosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Section(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(body, style = MaterialTheme.typography.bodyMedium)
    }
}
