package no.rusinnsikt.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

// Data models matching assets/substances.json (same content/schema as the
// iOS app's Resources/Substances.json). Parsed with org.json — no extra
// dependency needed, and no network access, ever: the file ships inside the
// app and is read once at launch, exactly like the iOS DataStore.

data class EmergencyNumber(
    val label: String,
    val number: String,
) {
    /** Digits-only tel: URI, e.g. "22 59 13 00" -> "tel:22591300". */
    val telUri: String?
        get() {
            val digits = number.filter { it.isDigit() || it == '+' }
            return if (digits.isEmpty()) null else "tel:$digits"
        }
}

data class EmergencyNumbers(
    val ambulance: EmergencyNumber,
    val poison: EmergencyNumber,
    val rusinfo: EmergencyNumber,
    val parorende: EmergencyNumber,
) {
    val all: List<EmergencyNumber> get() = listOf(ambulance, poison, rusinfo, parorende)
}

data class GeneralEmergencyGuidance(
    val title: String,
    val intro: String,
    val goldenRule: String,
    val ifUnconscious: List<String>,
    val ifConsciousButHeavilySedated: List<String>,
    val ifAgitatedOrHallucinating: List<String>,
    val afterCare: String,
    val mixingPrinciple: String,
)

data class Substance(
    val id: String,
    val name: String,
    val aliases: List<String>,
    val category: String,
    val riskLevel: String,
    val shortDescription: String,
    val effects: List<String>,
    val shortTermRisks: List<String>,
    val longTermRisks: List<String>,
    val overdoseSigns: List<String>,
    val emergencyAction: String,
    val mixingRisks: String,
    val legalStatus: String,
    val sourceNote: String,
) {
    /** Full lowercased text used for local, on-device search matching only. */
    val searchableText: String by lazy {
        (listOf(name, category, riskLevel, shortDescription, "overdose", "nødhjelp") + aliases + effects +
            shortTermRisks + longTermRisks + overdoseSigns + listOf(emergencyAction, mixingRisks, legalStatus))
            .joinToString(" ")
            .lowercase()
    }

    /** A small, high-signal word pool used for typo-tolerant fuzzy matching. */
    val fuzzyMatchWords: List<String> by lazy {
        (listOf(name, category, "overdose", "nødhjelp") + aliases)
            .joinToString(" ")
            .lowercase()
            .split(Regex("[^\\p{L}\\p{Nd}]+"))
            .filter { it.isNotEmpty() }
    }
}

data class SubstanceDatabase(
    val emergencyNumbers: EmergencyNumbers,
    val generalEmergencyGuidance: GeneralEmergencyGuidance,
    val substances: List<Substance>,
)

private fun JSONArray.toStringList(): List<String> = List(length()) { getString(it) }

private fun JSONObject.toEmergencyNumber(key: String): EmergencyNumber {
    val o = getJSONObject(key)
    return EmergencyNumber(label = o.getString("label"), number = o.getString("number"))
}

/**
 * Pure JSON-string parsing, with no Context/AssetManager dependency, so it
 * can be exercised directly from local JUnit tests (see
 * src/test/.../SubstancesJsonTest.kt) against the real assets/substances.json
 * on disk, exactly like SubstanceRepository.load() does at runtime.
 */
fun parseSubstanceDatabase(json: String): SubstanceDatabase {
    val root = JSONObject(json)

    val numbersJson = root.getJSONObject("emergencyNumbers")
    val numbers = EmergencyNumbers(
        ambulance = numbersJson.toEmergencyNumber("ambulance"),
        poison = numbersJson.toEmergencyNumber("poison"),
        rusinfo = numbersJson.toEmergencyNumber("rusinfo"),
        parorende = numbersJson.toEmergencyNumber("parorende"),
    )

    val guidanceJson = root.getJSONObject("generalEmergencyGuidance")
    val guidance = GeneralEmergencyGuidance(
        title = guidanceJson.getString("title"),
        intro = guidanceJson.getString("intro"),
        goldenRule = guidanceJson.getString("goldenRule"),
        ifUnconscious = guidanceJson.getJSONArray("ifUnconscious").toStringList(),
        ifConsciousButHeavilySedated = guidanceJson.getJSONArray("ifConsciousButHeavilySedated").toStringList(),
        ifAgitatedOrHallucinating = guidanceJson.getJSONArray("ifAgitatedOrHallucinating").toStringList(),
        afterCare = guidanceJson.getString("afterCare"),
        mixingPrinciple = guidanceJson.getString("mixingPrinciple"),
    )

    val substancesJson = root.getJSONArray("substances")
    val substances = List(substancesJson.length()) { i ->
        val s = substancesJson.getJSONObject(i)
        Substance(
            id = s.getString("id"),
            name = s.getString("name"),
            aliases = s.getJSONArray("aliases").toStringList(),
            category = s.getString("category"),
            riskLevel = s.getString("riskLevel"),
            shortDescription = s.getString("shortDescription"),
            effects = s.getJSONArray("effects").toStringList(),
            shortTermRisks = s.getJSONArray("shortTermRisks").toStringList(),
            longTermRisks = s.getJSONArray("longTermRisks").toStringList(),
            overdoseSigns = s.getJSONArray("overdoseSigns").toStringList(),
            emergencyAction = s.getString("emergencyAction"),
            mixingRisks = s.getString("mixingRisks"),
            legalStatus = s.getString("legalStatus"),
            sourceNote = s.getString("sourceNote"),
        )
    }

    return SubstanceDatabase(numbers, guidance, substances)
}

object SubstanceRepository {
    @Volatile private var cached: SubstanceDatabase? = null

    fun load(context: Context): SubstanceDatabase {
        cached?.let { return it }
        synchronized(this) {
            cached?.let { return it }
            val text = context.assets.open("substances.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val database = parseSubstanceDatabase(text)
            cached = database
            return database
        }
    }
}

/** The one locally stored preference — whether onboarding has been seen. Nothing else persists on-device. */
object OnboardingState {
    private const val PREFS = "rusinnsikt_prefs"
    private const val KEY_HAS_SEEN_ONBOARDING = "hasSeenOnboarding"

    fun hasSeenOnboarding(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_HAS_SEEN_ONBOARDING, false)

    fun setHasSeenOnboarding(context: Context, value: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_HAS_SEEN_ONBOARDING, value).apply()
    }

    /** Wipes the one locally stored preference — full reset of the app's on-device footprint. */
    fun clearAll(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
