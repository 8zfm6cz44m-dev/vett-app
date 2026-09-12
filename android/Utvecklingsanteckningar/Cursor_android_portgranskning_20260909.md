---
name: Cursor Android-portgranskning Rusinnsikt 2026-09-09
description: Skrivskyddad jämförelse av Android-porten mot iOS-källan. Ingen produktionskod ändrad. Tester och emulator inte körda.
---

# Rusinnsikt (Android) — portgranskning mot iOS (Cursor, 2026-09-09)

**Vad det är:** Android-port av iOS-appen Rusinnsikt (Xcode-target Vett). Visningsnamn Rusinnsikt, paket `no.rusinnsikt.app`, Kotlin + Jetpack Compose + Material 3. Harm-reduction-oppslagsverk med bundlad `substances.json` (28 stoffer), nödflik, fuzzy-søk, valgfri e-poststøtte. Ingen konto, ingen analytics-SDK, ingen Play Billing.

**Metod:** läst `android/app/src/main/**` mot `Vett/RusFaktaApp/**` och `VettTests/**`. Jämfört `Substances.json` mot `assets/substances.json` med `json.load` (identiska). Läst `AndroidManifest.xml`, `app/build.gradle.kts`, backup-XML, iOS-granskningen `Utvecklingsanteckningar/Cursor_kodgranskning_20260906.md`. Ingen `./gradlew test`, ingen emulator, ingen medicinsk faktagranskning av varje JSON-post.

**iOS-källa:** `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Vett`  
**Android-källa:** `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Vett/android`

---

## Sammanfattning

Porten är trogen, inte slarvig. Kärnlogiken är kopierad 1:1: Damerau-Levenshtein, AND-sökning på flera ord, överdos-autoscroll, `SubstanceGroup` med cannabis-override och poppers ≠ nikotin, onboarding + nollställ, personvern i appen, mailto istället för IAP, `ACTION_DIAL` istället för `CALL_PHONE`, ingen `INTERNET`-behörighet. `Substances.json` är byte-för-byte densamma (28 poster).

Det som kan sabba **användning i ett akut läge** är inte parsern utan **navigeringen**: iOS har «Se full nødhjelp-guide» på överdoskortet och behåller flikfältet. Android saknar länken och döljer flikarna på detaljsidan.

Det som kan sabba **långsiktig kvalitet** är att iOS redan har XCTest för sök, JSON och gruppering, medan Android bara har malltestet `2+2=4`. Två kopior av samma JSON utan CI-diff är den tysta innehållsrisken.

Inga P0 som kraschar faktasidorna vid normal start, givet att JSON fortsätter parsa (iOS-testerna täcker det; Android gör det inte).

Arkitekturen behöver inte Hilt, Room eller ViewModel. Appen är lika platt som iOS `DataStore`. Byt inte till arkitektur-teater — porta testerna, återställ nödhjelp-vägen, och stäng backup-/tillgänglighetshålen.

---

## Funktionsparitet

| Funktion | iOS | Android | Status |
|----------|-----|---------|--------|
| Flikar Fakta / Nødhjelp / Søk / Om | `TabView` + `NavigationStack` per flik | `NavigationBar` + `NavHost` | Paritet i flikar; avvikelse i stack (se P1) |
| Onboarding + nollställ | `@AppStorage("hasSeenOnboarding")` | `SharedPreferences` `rusinnsikt_prefs` | Paritet |
| 28 substanser + nöddata | `Resources/Substances.json` | `assets/substances.json` | Identisk |
| Fuzzy search | `FuzzySearch.swift` | `FuzzySearch.kt` | Paritet |
| Gruppering | `SubstanceGroup` i `SubstanceListView.swift` | `SubstanceGroup.kt` | Paritet |
| Överdos-autoscroll från sök | `ScrollViewReader` + 350 ms | Y-offset + 150 ms | Paritet (Android mäter bättre) |
| `tel:` / `mailto:` / dela text | `Link` / `ShareLink` | `ACTION_DIAL` / `SENDTO` / `SEND` | Paritet i avsikt; Android saknar try/catch |
| Personvern i appen | `PrivacyPolicyView` | `PrivacyPolicyScreen` | Paritet (UserDefaults → SharedPreferences i copy) |
| Støtte utan IAP | `TipJarView` mailto | `SupportScreen` mailto | Paritet |
| «Se full nødhjelp-guide» | `NavigationLink` i överdoskort | Saknas | Saknas |
| Flikfält på detaljsida | Kvar | Döljs | Avvikelse |
| VoiceOver / TalkBack | Sammansatta labels | Nästan inga | Avvikelse |
| Tester FuzzySearch / JSON / grupper | 4 XCTest-filer | Mall `ExampleUnitTest` | Saknas |
| Nätverk | Sandbox `ENABLE_*_NETWORK = NO` | Ingen `INTERNET`-behörighet | Paritet |

---

## 1. Bekräftade buggar / portluckor

### P1 — Nødhjelp är två steg bort från överdossidan

- **Filer:** `SubstanceDetailScreen.kt` (ingen länk), `MainActivity.kt` ~100, 128–147 (`showBottomBar` bara när `currentRoute` är en rotflik). **iOS:** `SubstanceDetailView.swift` ~90–96.
- **Utgångsläge:** iOS har `NavigationLink { EmergencyView() }` med texten «Se full nødhjelp-guide» under överdoskortet. TabView lämnar flikfältet synligt.
- **Vad som blir fel:** På Android måste användaren trycka Tillbaka och sedan byta till Nødhjelp. I ett akut läge är det den viktigaste vägen i appen.
- **Varför:** Detalj, personvern och støtte är stack-rutter. `showBottomBar = tabs.any { it.route == currentRoute }` döljer fältet medvetet.
- **Fix:** (1) Lägg till samma länk på överdoskortet, t.ex. `navController.navigate("nodhjelp")` eller en nestad `emergency`-rutt. (2) Visa flikfältet även på `detail/…`, eller navigera till Nødhjelp-fliken istället för att pusha en kopia.
- **Test:** UI-test: öppna Kokain via sök «overdose kokain» → överdoskortet har en klickbar väg till full guide utan att lämna detaljen via system-back.

### P1 — Ingen testdäckning för den svåra logiken

- **Filer:** `android/app/src/test/java/no/rusinnsikt/app/ExampleUnitTest.kt` (`2+2=4`). **iOS:** `VettTests/FuzzySearchTests.swift`, `SubstancesJSONTests.swift`, `SubstanceModelTests.swift`.
- **Utgångsläge:** iOS låser Levenshtein (transposition = 1), fuzzy-trösklar, JSON-decode, unika id, ifyllda overdoseSigns/emergencyAction, `tel:113` / `tel:22591300`, gruppering (poppers → annet, snus/vape → nikotin, cannabis → cannabinoider, kratom → opioider, xylazin/nps → annet).
- **Vad som blir fel:** En tyst avvikelse i `FuzzySearch`, `SubstanceGroup` eller JSON-parsern når Play Store. iOS `DataStore.init` `fatalError` fångas av `testDataStore_initializesWithoutCrashing`. Android `SubstanceRepository.load` kastar `JSONException` på huvudtråden utan motsvarande grind.
- **Varför:** Porten kommenterar «ported 1:1» men flyttade inte testsviten.
- **Test som borde finnas:** JUnit mot samma fall som iOS, med `assets/substances.json` som fixture (lägg JSON även under `src/test/resources` eller läs från assets via Robolectric). Statisk assert att iOS- och Android-JSON är identiska (CI-diff).

### P1 — `startActivity` utan skydd på nödnummer

- **Filer:** `NodhjelpScreen.kt` `dial()`, `SubstanceDetailScreen.kt` «Ring 113 nå», `OmScreen.kt` `NumberRow` + mailto, `SupportScreen.kt` mailto, `SubstanceDetailScreen.kt` / `OmScreen.kt` share.
- **Utgångsläge:** `context.startActivity(Intent(ACTION_DIAL, …))` och `ACTION_SENDTO` / `ACTION_SEND` utan `try/catch`.
- **Vad som blir fel:** `ActivityNotFoundException` på surfplatta, emulator, Chromebook eller enhet utan telefon-/e-postapp. Appen kraschar just när 113 ska visas.
- **Varför:** iOS `Link` / `ShareLink` failar tystare. Android måste hantera saknad aktivitet själv.
- **Fix:** `try/catch` + visa numret i en Snackbar/dialog om dialern saknas. Dela via `createChooser` är redan rätt mönster men kan fortfarande kasta.
- **Test:** Enhetstest av `EmergencyNumber.telUri` (redan i iOS). Manuellt: emulator utan telefonapp.

### P1 — TalkBack saknar iOS VoiceOver-semantik

- **Filer:** `FaktaListScreen.kt` `SubstanceRow` / sökikon, `NodhjelpScreen.kt` `CallButton` / steg, `OmScreen.kt` `NumberRow`. **iOS:** `SubstanceListView.swift` ~252–253, `EmergencyView.swift` ~75–78, 106–107, 144–147, `AboutView.swift` ~91–94.
- **Utgångsläge:** iOS slår ihop varje substansrad till «namn, kategori. kort beskrivning» och varje samtalsknapp till «etikett, nummer» + hint «Ring nå» + `.isButton`. Android sätter `contentDescription = null` på ikoner (korrekt dekorativt) men ger inte sammansatta labels. Klickbara `Row` får bara standard-click-semantik.
- **Vad som blir fel:** TalkBack stannar på ikon, namn och beskrivning var för sig. Nödknappar läses inte som «Ambulanse, 113 — Ring nå».
- **Fix:** `Modifier.semantics { contentDescription = "…" }` på rad och knapp, samma strängar som iOS. `mergeDescendants` eller `clearAndSetSemantics`.
- **Test:** Manuell TalkBack-runda på Fakta-listan och Nødhjelp. Helst Compose UI-test på semantics.

### P1 — Onboarding ritar under systemfälten

- **Filer:** `MainActivity.kt` `enableEdgeToEdge()`, `RusinnsiktApp()` visar `OnboardingScreen` **utan** `Scaffold`. `OnboardingScreen.kt` är en `Column` med `padding(24.dp)`.
- **Utgångsläge:** Huvudskalen (`MainNavHost`) får `innerPadding` från Scaffold. Välkomstskärmen gör det inte.
- **Vad som blir fel:** Titel, första stycke och/eller knappen «Jeg forstår, fortsett» kan hamna under statusfält eller gestnavigering. Första intrycket — och App Review-skärmen — är den som klipps.
- **Fix:** `Modifier.windowInsetsPadding(WindowInsets.systemBars)` eller lägg onboarding i samma Scaffold. Verifiera på en gest-nav-enhet, inte bara med treknappars-nav.
- **Test:** Manuellt på API 35+ med edge-to-edge. Skärmdump av första launch.

### P1 — Backup motstrider personverntexten

- **Filer:** `AndroidManifest.xml` `android:allowBackup="true"`, `res/xml/backup_rules.xml` (Android Studio-mall, allt utkommenterat), `res/xml/data_extraction_rules.xml` (TODO). **Copy:** `PrivacyPolicyScreen.kt` «sendes aldri noe sted», `OnboardingScreen.kt` «sender ingen data noe sted».
- **Utgångsläge:** Tomma cloud-backup-regler betyder standardbeteende: SharedPreferences kan synkas till användarens Google-konto (Auto Backup / cloud-backup).
- **Vad som blir fel:** Den enda lokala inställningen (`hasSeenOnboarding`) kan lämna enheten. Policyn och onboarding ljuger då, på samma sätt som iOS-granskningen 2026-09-06 påpekade Om-copy mot UserDefaults — den iOS-texten är sedan rättad, men Android återinför läckan via backup.
- **Fix:** `android:allowBackup="false"` (enklast, matchar «ingenting lämnar telefonen») **eller** exkludera `rusinnsikt_prefs` i både `backup_rules.xml` och `data_extraction_rules.xml`. Uppdatera personverntexten om backup medvetet behålls.
- **Test:** Ingen kodtest. Play Data safety-formuläret måste stämma med valet.

### P1 — Två kopior av Substances.json utan skydd mot drift

- **Filer:** `Vett/RusFaktaApp/Resources/Substances.json` och `android/app/src/main/assets/substances.json`.
- **Utgångsläge:** Identiska 2026-09-09 (28 id, samma ordning). Laddas oberoende: iOS `JSONDecoder`, Android manuell `org.json`.
- **Vad som blir fel:** En faktarättelse på ena plattformen når inte den andra. Det är den värsta klassen av bugg för den här appen — tyst, medicinskt relevant, osynlig utan diff.
- **Fix:** En källa (t.ex. repo-rot `Substances.json`) som båda byggen kopierar, plus CI-steg `diff` / checksum. Alternativt ett test som failar om filerna skiljer sig.
- **Test:** `diff` i CI. JUnit som räknar 28 unika id och samma fältkrav som `SubstancesJSONTests.swift`.

---

## 2. Viktiga testluckor (Android mot iOS-sviten)

Porta dessa fall rakt av. De är redan skrivna mot rätt beteende.

### FuzzySearch (`FuzzySearchTests.swift`)

- Identiska strängar → avstånd 0.
- «ovre» mot «over» → 1 (transposition, inte 2).
- En substitution / insertion / deletion → 1.
- Tomma strängar.
- Exakt substring matchar alltid.
- Query &lt; 3 tecken: bara exact substring, ingen fuzzy («kx» mot kandidat «kokain» ska faila).
- «ovr» mot kandidater `overdose`/`nødhjelp` matchar trots att fullText saknar ordet.
- «banan» matchar inte samma kandidater.
- Prefix: «keta» mot «ketamin».

### JSON + gruppering (`SubstancesJSONTests.swift`)

- Decode/parse utan fel.
- Inte tom.
- Unika `id`.
- Inget tomt `name` / `shortDescription` / `overdoseSigns` / `emergencyAction`.
- Fyra nödnummer; ambulans `"113"` → `tel:113`; Giftinformasjonen → `tel:22591300`.
- poppers → `ANNET`, snus + e-sigaretter → `NIKOTIN`, cannabis → `CANNABINOIDER`.
- kratom → `OPIOIDER` (kategoristrängen innehåller «opioid»).
- xylazin + nps → `ANNET`.
- `SubstanceRepository.load` kastar inte (motsvarar `DataStore()`).

### Modell (`SubstanceModelTests.swift`)

- `searchableText` innehåller namn, alias, injicerade «overdose» och «nødhjelp», och är lowercased.
- `fuzzyMatchWords` innehåller namn/alias men **inte** långa risk-/effektord («langsiktig»).
- Tomt nödnummer → `telUri == null`.

Android `Substance` är `data class` (equals på alla fält). iOS `==` / hash är bara `id`. Irrelevant för Compose-nycklar (`key = { it.id }`) men ska inte kopieras blint om någon senare stoppar `Substance` i en `Set`.

---

## 3. Saker som kräver fysisk enhet / Play Review

- `tel:113` och Giftinformasjonen `tel:22591300` (mellanslag tas bort i `EmergencyNumber.telUri`). Emulator ringer inte.
- TalkBack på nödknappar och substansrader — se P1.
- Edge-to-edge: onboarding, sökfält + tangentbord (`windowSoftInputMode="adjustResize"`), gestnav.
- Rotation: söktext och överdos-scroll (se P2).
- Surfplatta / enhet utan telefonapp: P1-krasch.
- Play Data safety: ingen insamling, men backup-valet måste stämma.
- Play-listing: 17+, personvern-URL (iOS har utkast i `docs/privacy.html`; Android har bara in-app-text). Publik URL krävs för butiken, inte för appen.
- Crack-postens tillverkningsspråk ligger kvar i den delade JSON:en — samma App Review-risk som i iOS-granskningen 2026-09-06. Medvetet behållet då; gäller båda plattformarna.

---

## 4. Delar som verifierats som korrekta (kod, inte kört)

- **Offline kärna:** `SubstanceRepository` läser bara `assets/substances.json`. Ingen Retrofit/OkHttp/Firebase. Manifestet deklarerar inga behörigheter. Inga tredjeparts-SDK:er utöver AndroidX/Compose.
- **Nød:** egen flik, 113 + Giftinformasjonen överst, röd gyllene regel, tre scenariosektioner, eftervård, blandningsprincip, alla fyra nummer. Detaljsida: rött överdoskort + hårdkodad `tel:113` (samma som iOS).
- **Sök:** query splittas på mellanslag; varje ord måste matcha (`all`). Fuzzy bara mot `fuzzyMatchWords` (namn/kategori/alias + «overdose»/«nødhjelp»). Lång brödtext bara via `searchableText.contains`. Överdosnyckelord `overdose / nødhjelp / akutt / død / forgiftning` — samma lista som iOS.
- **Gruppering:** nio grupper i samma enum-ordning. Keyword-prioritet identisk. `cannabis`-id-override. `innåndingsmiddel` → ANNET.
- **Onboarding + reset:** samma copy (17+, skadereduktion, offline, ingen konto). Om → «Nullstill velkomstskjerm» anropar `OnboardingState.clearAll` och visar välkomsten igen.
- **Støtte gated:** bara från Om, ingen paywall på fakta/nød. Copy nämner Play Store istället för App Store — rätt anpassning.
- **Share:** helsides plaintext, ingen Play Store-länk. Footer samma som iOS.
- **Tema:** fast indigo, ingen Material You dynamic color — medvetet som iOS `.tint(.indigo)`.
- **Navigationsstate på flikar:** `popUpTo(start) { saveState = true }`, `launchSingleTop`, `restoreState` — Fakta och Søk behåller var sitt sökfält, som iOS två instanser av `SubstanceListView`.
- **Överdos-scroll:** `onGloballyPositioned` + `positionInParent()` är mer robust än iOS procentgissning när sektionerna är olika långa.
- **Parser:** alla JSON-fält som iOS `Codable`-modellerna kräver läses med `getString` / `getJSONArray` (fail-closed, ingen tyst default).
- **telUri:** behåller siffror och `+`, strippar mellanslag — «22 59 13 00» → `tel:22591300`.

Kända medvetna avvägningar (samma som iOS): två flikar med samma lista (Fakta vs Søk med autofokus) — separat sökstate. Krascha högt om JSON saknas — fail-closed, men utan Android-test som fångar det före release.

---

## 5. Mindre (P2/P3)

- **P2** Söktext i `remember`, inte `rememberSaveable`. Rotation / processdöd rensar query och därmed `mentionsOverdose` innan användaren öppnat detaljen.
- **P2** Ogiltigt `substanceId` i ruten `detail/{id}/{highlight}` renderar tom `NavHost`-destination. iOS skickar hela `Substance`. Visa feltext + tillbaka.
- **P2** Sortering: iOS `localizedCompare`, Android `name.lowercase()` (kodpunktsordning). Nuvarande namnlista har inga inledande æ/ø/å, men pariteten bryts när sådana tillkommer. Använd `Collator.getInstance(Locale("nb", "NO"))`.
- **P2** JSON parsas på huvudtråden vid första `load`. ~59 kB — acceptabelt. Cachen (`@Volatile` + double-check) är korrekt. Anropas från flera composables; andra anropet är gratis.
- **P2** `OnboardingState.setHasSeenOnboarding` använder `apply()` (asynkront). In-memory-flaggan uppdateras direkt; risken att processen dör innan write är teoretisk.
- **P3** Oanvänd import `HelpOutline` i `FaktaListScreen.kt`. `SubstanceGroup.iconKey` kopierad från iOS men oanvänd (ikoner mappas i `iconFor()`).
- **P3** Inga `@Preview`-composables (iOS har `#Preview`).
- **P3** `res/values/colors.xml` har kvar mallens lila/teal. Compose-temat använder `Color.kt`. XML-tema `Theme.Rusinnsikt` är Light Material — möjlig mismatch på splash/systembar innan Compose ritar.
- **P3** Alla UI-strängar hårdkodade på norska i Kotlin, inte `strings.xml`. Samma som iOS (hårdkodat i Swift). OK tills någon vill översätta.
- **P3** R8/minify avstängt i release. `keepRules/rules.keep` är tom mall. Inget funktionsfel.
- **P3** Ingen Android-buildguide. `README_BUILD_GUIDE.md` i iOS-roten är Xcode-only.
- **P3** `lifecycle-runtime-ktx` i Gradle men inga ViewModels. Harmless.
- **P2 kliniskt (inte compiler, samma som iOS):** «Hvis personen er våken, men svært sløv» → ge mat/drikke. Aspirationsrisk. Innehåll i JSON, inte Kotlin. Gäller båda apparna.

---

## 6. Arkitektur (medvetet enkelt — behåll det)

```
MainActivity
  RusinnsiktApp          onboarding-grind
    MainNavHost          Scaffold + 4 flikar + stack
      FaktaListScreen / NodhjelpScreen / OmScreen / …
      SubstanceDetailScreen / PrivacyPolicyScreen / SupportScreen

SubstanceRepository      cachat parse av assets/substances.json
OnboardingState          en SharedPreferences-boolean
FuzzySearch              ren funktion, ingen Android-beroende
SubstanceGroup           ren funktion, ingen Android-beroende
```

- Ett Gradle-modul `:app`. `minSdk 24`, `targetSdk 37`, `versionCode 1`, `versionName "1.0"`.
- Ingen DI, ingen databas, ingen ViewModel. Compose-skärmar anropar repository direkt. Det speglar iOS `@EnvironmentObject DataStore`.
- `rememberSaveable` på sök räcker. ViewModel är valfritt, inte nödvändigt.
- `org.json` istället för kotlinx.serialization: extra beroende undviks. Kostnaden är manuell parser utan schema-test — därav P1 om tester.

---

## 7. Innan Play Store (kort)

1. Återställ «Se full nødhjelp-guide» och besluta om flikfältet ska synas på detaljsidan.  
2. `try/catch` på tel/mailto/share; visa numret om dialern saknas.  
3. Porta iOS-testerna till JUnit. Lägg CI-diff på de två JSON-filerna.  
4. TalkBack-labels på rader, samtalsknappar och nödhjelp-steg.  
5. `WindowInsets` på onboarding.  
6. `allowBackup=false` **eller** exkludera `rusinnsikt_prefs`; rätta Data safety + personverncopy.  
7. Publik personvern-URL till Play Console (samma behov som App Store Connect; utkast finns i iOS `docs/privacy.html`).  
8. `rememberSaveable` för sök. Fel-UI för okänt id. `Collator` nb-NO.  
9. Manuell runda: 113, Giftinformasjonen, sök «overdose kokain» → autoscroll, nollställ onboarding, dela, mailto, rotation, TalkBack, edge-to-edge.  
10. Crack-meningen i JSON — samma medvetna App Review-risk som på iOS.

Jag har inte kört `./gradlew test` eller emulator, och inte bedömt om varje JSON-post medicinskt stämmer mot rusinfo.no.

---

## 8. Filkarta (Android, de som granskats)

| Sökväg | Roll |
|--------|------|
| `app/src/main/java/no/rusinnsikt/app/MainActivity.kt` | Entry, onboarding-grind, NavHost, flikfält |
| `app/src/main/java/no/rusinnsikt/app/data/Models.kt` | Modeller, `SubstanceRepository`, `OnboardingState` |
| `app/src/main/assets/substances.json` | Innehåll (28 substanser + nöddata) |
| `app/src/main/java/no/rusinnsikt/app/logic/FuzzySearch.kt` | Typotolerant sök |
| `app/src/main/java/no/rusinnsikt/app/logic/SubstanceGroup.kt` | Listgruppering |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/FaktaListScreen.kt` | Lista + sök (Fakta och Søk) |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/SubstanceDetailScreen.kt` | Detalj, överdoskort, share |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/NodhjelpScreen.kt` | Nödflik |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/OnboardingScreen.kt` | Första launch |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/OmScreen.kt` | Om, reset, mailto, share |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/PrivacyPolicyScreen.kt` | Personvern i appen |
| `app/src/main/java/no/rusinnsikt/app/ui/screens/SupportScreen.kt` | Valfri e-poststøtte |
| `app/src/main/java/no/rusinnsikt/app/ui/theme/Theme.kt` | Fast indigo, mörkt/ljust |
| `app/src/main/AndroidManifest.xml` | En activity, backup på, inga permissions |
| `app/src/main/res/xml/backup_rules.xml` | Oifylld mall |
| `app/src/main/res/xml/data_extraction_rules.xml` | TODO-mall |
| `app/src/test/.../ExampleUnitTest.kt` | Stubbe |

Motsvarande iOS-sanning: `Vett/RusFaktaApp/` och `VettTests/`.

---

## 9. Rekommenderad ordning (när kod ska ändras)

Prioriterat efter skada i ett akut läge, sedan regressionsrisk, sedan Play-polish.

1. «Se full nødhjelp-guide» + behåll flikfältet på detaljsidan.  
2. Fånga `ActivityNotFoundException` på tel/mailto/share; visa numret.  
3. Porta FuzzySearch- / JSON- / modelltesterna. CI-diff på JSON.  
4. TalkBack-semantik (samma texter som iOS).  
5. WindowInsets på onboarding. Stäng eller exkludera backup.  
6. `rememberSaveable`, fel-UI för okänt id, norsk `Collator`.
