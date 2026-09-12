package no.rusinnsikt.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.rusinnsikt.app.util.safeStartActivity

/**
 * Optional support screen — no in-app purchase, ported 1:1 from
 * TipJarView.swift (which itself deliberately has no Play Billing, so the
 * app stays fully usable with no network / no Play Store connection).
 */
@Composable
fun SupportScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Støtt Rusinnsikt", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(
                "Rusinnsikt er gratis og skal virke uten internett — fakta, søk og nødhjelp er aldri bak betaling. Derfor er det ingen kjøp i appen (det ville krevd tilkobling til Play Store).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Vil du likevel bidra til vedlikehold, send en e-post. Det åpner e-post-appen på telefonen; ingenting sendes før du selv trykker send.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:elofsson.martin@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Støtte til Rusinnsikt")
                }
                safeStartActivity(context, intent, "Fant ingen e-post-app. Send til elofsson.martin@gmail.com manuelt.")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Email, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Send e-post om støtte", fontWeight = FontWeight.SemiBold)
        }
    }
}
