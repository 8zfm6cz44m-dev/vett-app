---
name: Cursor Android-portgranskning pass 2 (efter Claude) 2026-09-09
description: Uppföljning av Cursor_android_portgranskning_20260909.md efter att Claude uppdaterat Android-koden. Ingen produktionskod ändrad. Gradle-tester inte körda (ingen JRE i granskningsmiljön).
---

# Rusinnsikt (Android) — portgranskning pass 2, efter Claude (Cursor, 2026-09-09)

**Föregående:** `Utvecklingsanteckningar/Cursor_android_portgranskning_20260909.md`  
**iOS-källa:** `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Vett`  
**Android-källa:** `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Vett/android`

**Metod:** ominläst `android/app/src/main/**` och nya `android/app/src/test/**`. Jämfört mot förra P1–P3-listan och mot `Vett/RusFaktaApp/**` + `VettTests/**`. JSON jämförd med `json.load` — fortfarande identisk (28 poster). `./gradlew test` misslyckades i den här miljön (`Unable to locate a Java Runtime`). Ingen emulator. Ingen medicinsk faktagranskning.

**Nya filer sedan pass 1:** `util/IntentUtils.kt`, `FuzzySearchTest.kt`, `SubstancesJsonTest.kt`, `SubstanceModelTest.kt`. `ExampleUnitTest.kt` är borttagen.

---

## Sammanfattning

Claude har stängt **alla sju P1 från pass 1**. Porten är nu i princip feature-komplett mot iOS, med samma testdäckning för sök/JSON/gruppering och med nödhjelp ett tryck bort från överdoskortet.

Kvar är mest **navigationssemantik** (länken byter flik istället för att pusha, som iOS) och **underhåll** (två JSON-filer utan CI-diff). Inget av det är release-blocker för normal användning. Inga nya P1.

**Status mot pass 1:** 7/7 P1 åtgärdade · 4/4 P2 i förra listan åtgärdade eller nedgraderade · nya P2 är mindre.

---

## Status mot pass 1

| Pass 1 | Status nu | Kommentar |
|--------|-----------|-----------|
| P1 Nødhjelp två steg bort | **Åtgärdad** | «Se full nødhjelp-guide» + flikfält synligt på detalj. Se P2 om back-stack. |
| P1 Inga tester | **Åtgärdad** | Tre JUnit-klasser, 1:1 mot iOS. `parseSubstanceDatabase` extraherad för test utan Context. |
| P1 `startActivity` krasch | **Åtgärdad** | `safeStartActivity` + Toast med numret. Alla tel/mailto/share. |
| P1 TalkBack | **Åtgärdad** | Sammansatta labels på rader, nödknappar, steg, Om-nummer. En rest på «Ring 113 nå» (P3). |
| P1 Onboarding under systemfält | **Åtgärdad** | `windowInsetsPadding(WindowInsets.systemBars)`. |
| P1 Backup vs personvern | **Åtgärdad** | `allowBackup="false"`. Policyn stämmer. |
| P1 Två JSON utan skydd | **Nedgraderad till P2** | Android-JSON låses av tester. Ingen CI-diff mot iOS-filen. |
| P2 Sök tappas vid rotation | **Åtgärdad** | `rememberSaveable`. |
| P2 Tom skärm vid okänt id | **Åtgärdad** | `UnknownSubstanceScreen`. |
| P2 Sortering utan nb-NO | **Åtgärdad** | `Collator.getInstance(Locale("nb", "NO"))`. |
| P3 Oanvänd `HelpOutline` i Fakta | **Kvar** | Importen finns kvar. |

---

## Funktionsparitet (uppdaterad)

| Funktion | iOS | Android | Status |
|----------|-----|---------|--------|
| Flikar Fakta / Nødhjelp / Søk / Om | TabView | NavigationBar | Paritet |
| Flikfält på detaljsida | Kvar | Kvar | Paritet |
| «Se full nødhjelp-guide» | Push `EmergencyView` på samma stack | Byter till fliken Nødhjelp (`popUpTo` start) | Finns; back-beteende skiljer (P2) |
| Onboarding + nollställ | AppStorage | SharedPreferences | Paritet |
| 28 substanser | `Substances.json` | `assets/substances.json` | Identisk |
| Fuzzy search + gruppering | Swift | Kotlin | Paritet + tester |
| Överdos-autoscroll | ScrollViewReader 350 ms | Y-offset + 150 ms | Paritet |
| tel / mailto / share | Link / ShareLink | `safeStartActivity` | Paritet + robustare felväg |
| VoiceOver / TalkBack | Sammansatta labels | Samma texter på lista/nød/Om | Paritet (en lucka på detalj-113) |
| Tester FuzzySearch / JSON / grupper | XCTest | JUnit | Paritet i fall |
| Nätverk | Sandbox av | Ingen INTERNET | Paritet |
| Backup | UserDefaults lokalt | `allowBackup=false` | Paritet mot «sendes aldri» |

---

## 1. Kvarvarande fynd

### P2 — «Se full nødhjelp-guide» byter flik, pushar inte

- **Filer:** `MainActivity.kt` ~201–207, `SubstanceDetailScreen.kt` ~228–251. **iOS:** `SubstanceDetailView.swift` ~90–96 (`NavigationLink { EmergencyView() }`).
- **Utgångsläge:** Android anropar `navigate("nodhjelp") { popUpTo(start) { saveState = true }; launchSingleTop; restoreState }`. Det är samma mönster som att trycka på Nødhjelp-fliken.
- **Vad som blir fel:** Tillbaka från Nødhjelp återvänder **inte** till substanssidan, till skillnad från iOS. Detaljstacken poppas. Användaren landar på Fakta-listan (eller app-exit beroende på back-stack).
- **Varför det inte är P1:** Vägen *till* full guide är ett tryck. Flikfältet syns också, så Nødhjelp går att nå utan länken. Akutläget är löst.
- **Fix (om paritet önskas):** nestade grafer per flik, eller pusha en `nodhjelp`-destination ovanpå detaljen utan `popUpTo`. Då fungerar system-back som på iOS.
- **Relaterat:** På `detail/{id}/{highlight}` är **ingen flik markerad**. `hierarchy` innehåller inte `fakta`/`sok` eftersom rutterna är syskon, inte nästade. Rent visuellt.

### P2 — Två kopior av Substances.json, ingen plattforms-diff

- **Filer:** `Vett/.../Resources/Substances.json` och `android/.../assets/substances.json`. Tester läser bara Android-filen via `File("src/main/assets/substances.json")`.
- **Utgångsläge:** Identiska 2026-09-09. `parseSubstanceDatabase` + `SubstancesJsonTest` skyddar Android-kopian mot intern regression.
- **Vad som blir fel:** En iOS-faktarättelse når inte Android (och tvärtom) utan manuell kopia. Tyst, medicinskt relevant.
- **Fix:** En källa + copy i båda byggen, eller CI `diff` / checksum mellan filerna. Ett test som failar om de skiljer sig (kräver att iOS-filen syns från Android-modulen, t.ex. `../../Vett/RusFaktaApp/Resources/Substances.json`).

### P3 — «Ring 113 nå» på detaljsidan saknar sammansatt TalkBack

- **Fil:** `SubstanceDetailScreen.kt` ~210–223. Jämför `NodhjelpScreen.CallButton` som har `mergeDescendants`, `Role.Button` och `onClickLabel = "Ring nå"`.
- **Utgångsläge:** Klickbar `Row` med ikon + text, `safeStartActivity` på plats, men ingen `semantics`.
- **Vad som blir fel:** TalkBack kan läsa ikon och «Ring 113 nå» som två stopp. Mindre än pass 1:s lista-lucka.
- **Fix:** Samma semantics-block som `CallButton`.

### P3 — Döda import / mall-XML

- `FaktaListScreen.kt`: oanvänd `HelpOutline`.
- `MainActivity.kt`: oanvänd `NavHostController`.
- `backup_rules.xml` / `data_extraction_rules.xml` är fortfarande Android Studio-mallar. Harmless när `allowBackup=false`. På API 31+ styrs device-to-device-transfer av `data-extraction-rules`; sektionen är utkommenterad (plattformens default). Onboarding-flaggan är inte känslig. Rensa mallarna om du vill att filerna ska säga sanningen.

### P3 — Release / butik (oförändrat)

- R8 avstängt. Ingen Android-buildguide. Ingen publik personvern-URL till Play Console (utkast i iOS `docs/privacy.html`).
- Personvernskärmen säger fortfarande «Sist oppdatert 6. september 2026» trots att backup-beteendet ändrats 9 september — copy om lagring stämmer ändå.
- Crack-postens tillverkningsspråk i den delade JSON:en — samma medvetna App Review-risk som på iOS.
- Inga `@Preview`. `colors.xml` har kvar mallfärger.

### P3 — Toast som fallback

- `safeStartActivity` visar `Toast.LENGTH_LONG` med numret. Rätt mot krasch. En Toast kan missas i stress. Snackbar/dialog vore tydligare, inte nödvändigt för v1.

---

## 2. Vad som verifierats som korrekt i den nya koden

### Tester (lästa, inte körda)

`FuzzySearchTest` speglar `FuzzySearchTests.swift`: identitet, transposition «ovre»/«over» = 1, substitution/insertion/deletion, tomma strängar, exact substring, kort query utan fuzzy, «ovr» mot overdose-kandidater, «banan» negativ, prefix «keta»/«ketamin».

`SubstancesJsonTest` speglar `SubstancesJSONTests.swift`: parse, icke-tom, unika id, kärnfält, `tel:113` / `tel:22591300`, poppers→ANNET, snus/vape→NIKOTIN, cannabis→CANNABINOIDER, kratom→OPIOIDER, xylazin/nps→ANNET. Läser den riktiga asset-filen, inte en kopia.

`SubstanceModelTest` speglar `SubstanceModelTests.swift` för searchableText / fuzzyMatchWords / tom telUri. `equatableAndHashable_useAllFields` dokumenterar medvetet att Kotlin `data class` jämför alla fält, till skillnad från iOS `==` på bara `id`. Rätt — inte en tyst paritetsmiss.

`parseSubstanceDatabase(json:)` är rätt utbrytning: samma parser som runtime, testbar utan Robolectric.

### Nødhjelp-väg

- Länk under `emergencyAction`, röd, «Se full nødhjelp-guide» + pil.
- `showBottomBar` är false bara för `privacy` och `support`.
- Tillbaka-pil när `!isTabRoot`.

### Intent-säkerhet

Alla anrop går via `safeStartActivity`. Fallback-texter innehåller numret eller e-postadressen («Ring 113 manuelt», «Ring $displayNumber manuelt», mailto-adressen).

### Tillgänglighet

Samma strängmall som iOS: `"\(name), \(category). \(shortDescription)"`, `"\(label), \(number)"` + «Ring nå», `"Steg N: …"`. `Role.Button` där det klickas.

### Övrigt som pass 1 redan godkände

Offline, ingen INTERNET, `ACTION_DIAL`, mailto-støtte, identisk JSON, fuzzy + gruppering, överdos-scroll via Y-offset, onboarding-copy, share utan butikslänk.

---

## 3. Saker som kräver fysisk enhet / Play Review

Samma lista som pass 1, plus verifiering av det nya:

- TalkBack på substansrad, 113-knapp, «Se full nødhjelp-guide», steg 1–n.
- Edge-to-edge på onboarding (särskilt gestnav).
- Rotation: sök «overdose kokain» ska överleva.
- Enhet/emulator utan telefonapp: Toast med numret, ingen krasch.
- Flikfält synligt på detalj; tryck Nødhjelp därifrån.
- Back från Nødhjelp efter länken — bekräfta att du *inte* landar på substansen (medveten avvikelse).
- Play Data safety: ingen backup, ingen insamling.
- Publik personvern-URL. 17+.
- Crack-meningen i JSON.

---

## 4. Innan Play Store (kort, uppdaterad)

Klart sedan pass 1: nödhjelp-länk, tester, intent-skydd, TalkBack på kärnytor, onboarding-insets, `allowBackup=false`, rememberSaveable, okänt-id-UI, nb-NO-sortering.

Kvar valfritt / butik:

1. Bestäm om Nødhjelp-länken ska pusha (iOS-back) eller byta flik (nu). Markera rätt flik på detaljsidan om ni behåller flik-byte.  
2. CI-diff mellan de två `Substances.json`.  
3. Semantics på «Ring 113 nå». Ta bort oanvända importer.  
4. Publik personvern-URL i Play Console.  
5. Manuell runda: 113, Giftinformasjonen, sök + autoscroll, nollställ, dela, mailto, rotation, TalkBack, edge-to-edge, ingen-dialer.  
6. `./gradlew test` lokalt — inte kört i den här granskningen.  
7. Crack-meningen — samma medvetna risk som iOS.

---

## 5. Filkarta (ändrat sedan pass 1)

| Sökväg | Ändring |
|--------|---------|
| `MainActivity.kt` | Flikfält på detalj, `onNavigateToNodhjelp`, `UnknownSubstanceScreen` |
| `SubstanceDetailScreen.kt` | Nødhjelp-länk, `safeStartActivity` |
| `FaktaListScreen.kt` | `rememberSaveable`, `Collator` nb-NO, rad-semantics |
| `NodhjelpScreen.kt` | `safeStartActivity`, knapp/steg-semantics |
| `OmScreen.kt` | `safeStartActivity`, nummer-semantics |
| `SupportScreen.kt` | `safeStartActivity` |
| `OnboardingScreen.kt` | `WindowInsets.systemBars` |
| `Models.kt` | `parseSubstanceDatabase` utbruten |
| `util/IntentUtils.kt` | **Ny** |
| `AndroidManifest.xml` | `allowBackup="false"` |
| `src/test/.../FuzzySearchTest.kt` | **Ny** |
| `src/test/.../SubstancesJsonTest.kt` | **Ny** |
| `src/test/.../SubstanceModelTest.kt` | **Ny** |

---

Jag har inte kört testerna och inte bedömt om varje JSON-post medicinskt stämmer mot rusinfo.no. Inga nya P1. Porten är redo för manuell enhetsrunda och Play-förberedelse.
