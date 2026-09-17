# CLAUDE.md – Rusinnsikt (Vett)

**Projekt:** Rusinnsikt — norsk faktaoppslagsverk om rusmidler och nödhjälp
**Ägare:** Martin Elofsson / Devlyn IT Tjenester
**Vad det ÄR:** Två helt fristående, helt offline native-appar med identiskt innehåll:
- iOS: SwiftUI, `Vett/RusFaktaApp/`, projekt `Vett.xcodeproj`, bundle-id `no.rusinnsikt.app`
- Android: Kotlin/Jetpack Compose, `android/`, applicationId `no.rusinnsikt.app`
- Innehåll: `Vett/RusFaktaApp/Resources/Substances.json` och `android/app/src/main/assets/substances.json` — ska alltid vara byte-identiska.

**Vad det INTE är (en tidigare version av denna fil påstod fel):** Ingen webbapp, ingen backend, ingen server, ingen databas, ingen inloggning, inga betalningar, inga annonser, inga premium-nivåer, inget API. Allt sådant strider mot appens grundlöfte och får inte byggas in.

## Integritetsregler — absoluta, gäller varje ändring

Appens hela existensberättigande är att den är 100 % anonym. Den ska kunna ligga på en jobbmobil utan att appen själv lämnar ett enda spår utanför telefonen. Därför:

1. **Noll nätverk.** Ingen `URLSession`/`URLRequest`/WebView på iOS. Android-manifestet ska ALDRIG få `android.permission.INTERNET` — det är den hårdaste garantin som finns (OS:et blockerar då varje socket, även från bibliotek). Om en funktion "behöver internet" är svaret nej.
2. **Noll tredjepartsberoenden.** iOS: inga Swift-paket, inga Pods. Android: endast AndroidX/Compose/Material från Google + `org.json` i tester. Ingen Firebase, Crashlytics, analys, annons-SDK, crash-rapportering, "bara statistik".
3. **Noll identifierare.** Aldrig `identifierForVendor`, IDFA, ANDROID_ID, Advertising ID, App Tracking Transparency.
4. **Noll konto, noll IAP, noll Play Billing/StoreKit.** Stöd sker via `mailto:` som användaren själv skickar.
5. **Lagring = exakt en boolean** (`hasSeenOnboarding`) i UserDefaults/SharedPreferences. Inga favoriter, ingen sökhistorik, inga "senast lästa", ingen cache. Sökfältet ska aldrig persisteras.
6. **Noll loggning** i produktionskod: inga `print`/`NSLog`/`os_log`/`Log.*`.
7. **Noll systemintegrationer som exponerar innehåll:** ingen Spotlight/CoreSpotlight, inga App Intents/Siri/genvägar, inga widgets, inga notiser, inga bakgrundslägen, inga URL-scheman/deep links/universal links.
8. **Noll backup av appdata på Android:** `android:allowBackup="false"` + explicita `<exclude>` för alla domäner i både `backup_rules.xml` och `data_extraction_rules.xml` (cloud-backup OCH device-transfer).
9. **Innehållet ska inte synas i app-växlaren:** iOS `PrivacyCover` i `RusFaktaApp.swift` när `scenePhase != .active`; Android `setRecentsScreenshotEnabled(false)` (API 33+) i `MainActivity`. Använd INTE `FLAG_SECURE` — användare ska kunna skärmdumpa nödhjälp för att dela.
10. **Behörigheter: inga.** Inga `NS*UsageDescription`, inga `<uses-permission>`.
11. **Privacy manifest** (`PrivacyInfo.xcprivacy`): `NSPrivacyTracking=false`, tomma listor, endast UserDefaults-skälet `CA92.1`. App Store Connect: "No, we do not collect data from this app".

Varje ändring i endera appen ska speglas i den andra. Varje release ska föregås av grep-kontrollen i `PERSONVERN_AUDIT.md` (sektionen "Kontroll före varje release").

## Var saker finns
- App Store-förberedelser och checklista: `APP_STORE_PREP.md`
- Integritetsaudit och exakt formulerat användarlöfte: `PERSONVERN_AUDIT.md`
- Faktakoll/källor: Claude-projektet "App - Vett" (`claude/faktakoll-log.md`, `claude/kildereferanser-rusmidler.md`)
- Skärmdumpar för App Store: `App Store Screenshots/`

## Arbetssätt
- Svara Martin på svenska. Appens innehåll är på norsk bokmål.
- Verifierade rättelser görs direkt och rapporteras efteråt.
- Claude pushar inte till GitHub; Martin pushar själv från sin Mac.
- Commit-fotnot enligt sessionens instruktion (Co-Authored-By + Claude-Session).

**Senast uppdaterad:** 2026-09-17
