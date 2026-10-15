# CLAUDE.md – Rusinnsikt

**Projekt:** Rusinnsikt — norsk faktaoppslagsverk om rusmidler och nödhjälp
**Ägare:** Martin Elofsson / Devlyn IT Tjenester
**Vad det ÄR:** Två helt fristående, helt offline native-appar med identiskt innehåll:
- iOS: SwiftUI, `Rusinnsikt/RusFaktaApp/`, projekt `Rusinnsikt.xcodeproj`, bundle-id `no.rusinnsikt.app`
- Android: Kotlin/Jetpack Compose, `android/`, applicationId `no.rusinnsikt.app`
- Innehåll: `Rusinnsikt/RusFaktaApp/Resources/Substances.json` och `android/app/src/main/assets/substances.json` — ska alltid vara byte-identiska.

**Vad det INTE är (en tidigare version av denna fil påstod fel):** Ingen webbapp, ingen backend, ingen server, ingen databas, ingen inloggning, inga betalningar, inga annonser, inga premium-nivåer, inget API. Allt sådant strider mot appens grundlöfte och får inte byggas in.

## Integritetsregler — absoluta, gäller varje ändring

Appens hela existensberättigande är att den är 100 % anonym. Den ska kunna ligga på en jobbmobil utan att appen själv lämnar ett enda spår utanför telefonen. Därför:

1. **Noll nätverk.** Ingen `URLSession`/`URLRequest`/WebView på iOS. Android-manifestet ska ALDRIG få `android.permission.INTERNET` — det är den hårdaste garantin som finns (OS:et blockerar då varje socket, även från bibliotek). Om en funktion "behöver internet" är svaret nej. iOS har ingen motsvarande behörighet (alla iOS-appar får använda nätet), så där är garantin: noll nätverkskod i källan, noll beroenden, och kontrollskriptets binärkontroll (`nm -u`/`otool -L` på Release-binären) som visar att inga nätverksklasser refereras eller länkas.
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
- Verifierade rättelser görs direkt och rapporteras efteråt (gäller interaktiva sessioner på Macen, där tester och kontrollskript kan köras).
- Commit-fotnot enligt sessionens instruktion (Co-Authored-By + Claude-Session).

## Push-policy (beslutad 2026-10-15)

- **Ingen Claude-session pushar till GitHub** – varken Claude Code på Macen, Cowork-sessioner eller det schemalagda veckojobbet. Martin pushar själv från sin Mac.
- **Det schemalagda faktakoll-jobbet (Cowork, headless) är endast förslagsgivande:** det klonar `vett-app`, granskar och skriver förslag + loggpost i sin rapport, men committar och pushar inte. Förslagen läggs in av Martin eller en Claude Code-session på Macen, där `xcodebuild test` och `scripts/personvern-sjekk.sh` körs före commit.
- Martin bör pusha sina lokala commits innan nästa jobbkörning, annars granskar jobbet gammalt innehåll på GitHub. Jobbet ska alltid ange vilken commit det granskat.
- Jobbets prompt ändras bara av Martin eller en Cowork-session (`update_trigger`), inte från Claude Code.

## Verifieringskrav för innehållsändringar (faktakoll)

- En sakuppgift får ändras/föreslås som **verifierad** bara om (a) en primärkälla (Lovdata, norsk myndighet, RUSinfo/Helsedirektoratet, FHI, DMP) otvetydigt motsäger nuvarande text, eller (b) minst två oberoende källor ur kategori 1–3 i `Claude_kildereferanser-rusmidler.md` gör det. Annars: ingen ändring, bara en bevakningsnotis i loggen.
- **Juridisk status** (narkotikalisten, forskrifter, ikraftträdande) verifieras mot **Lovdatas öppna API** (`https://api.lovdata.no`, `/v1/publicData/list` → `/v1/publicData/get/<fil>`, NLOD 2.0) – inte mot lovdata.no, som blockerar automatisk hämtning. Nedladdning av dataset kräver Martins ok med filnamn och storlek.
- `legalStatus`, dödsfallssiffror och akut-/nödråd (`emergencyAction`, `overdoseSigns`, `generalEmergencyGuidance`, nödnummer) kräver alltid primärkälla och granskas av Martin innan de läggs in.
- `riskReduction` får aldrig innehålla doser eller mängder. Endast rena sakfel ändras – aldrig ton, struktur eller kod.

**Senast uppdaterad:** 2026-10-15

## Källan till sanning för dokumentation (2026-10-05)

Allt som rör faktakoll, källbas, App Store-guide och personvern-audit lever **i detta repo**, inte i Cowork-projektet "App - Vett":

- `Utvecklingsanteckningar/Claude_faktakoll-log.md` – logg över alla faktakoll-körningar (lägg till en ny sektion per körning)
- `Utvecklingsanteckningar/Claude_kildereferanser-rusmidler.md` – tillåtna/förbjudna källor, hämtningsbegränsningar (uppdatera `last_verified`)
- `Utvecklingsanteckningar/Claude_app-store-connect-guide.md` – fältguide för App Store Connect
- `PERSONVERN_AUDIT.md` – integritetsaudit och exakt löfte till användare

Cowork-projektets `claude/*.md` är bara pekare hit och ska inte uppdateras. En Cowork-session som behöver innehållet läser filerna från Macen (ansluten mapp) eller från GitHub (`vett-app`, privat). Ingen manuell synk ska behövas.
