package no.rusinnsikt.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Launches [intent] and swallows [ActivityNotFoundException] instead of
 * crashing. iOS's `Link`/`ShareLink` fail silently when there's no handler
 * (e.g. a tablet or Chromebook build with no phone/mail app); Android must
 * do the equivalent itself, since `startActivity` throws. On failure the
 * user still sees [fallbackMessage] (e.g. the phone number to dial by hand)
 * via a Toast, so the emergency info is never simply lost.
 */
fun safeStartActivity(context: Context, intent: Intent, fallbackMessage: String) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, fallbackMessage, Toast.LENGTH_LONG).show()
    }
}
