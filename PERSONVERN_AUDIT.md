# Rusinnsikt — integritets- och anonymitetsaudit

**Datum:** 2026-09-17
**Omfattning:** All källkod, all konfiguration och alla "regler" i både iOS-appen (`Vett/RusFaktaApp`, `Vett.xcodeproj`) och Android-appen (`android/`), per commit `739f92d`.
**Metod:** Varje källfil har lästs rad för rad (samtliga 27 Swift-/Kotlin-filer: 12 iOS, 15 Android, manifest, privacy manifest, byggfiler, beroendelista, backup-regler, projektinställningar), kompletterat med en mekanisk grep-kontroll mot 36 kända läckagevektorer (`scripts/personvern-sjekk.sh`). Plattformspåståenden är kontrollerade mot Apples, Googles och Microsofts (Intune) egen dokumentation.

---

## 1. Slutsatsen i en mening

**Appen själv lämnar inget spår utanför telefonen — det kan garanteras med koden i hand. Att ingen *någonsin* kan se att appen finns på en telefon kan däremot ingen app garantera, för det avgörs av telefonen, inte av appen.** Skillnaden är hela poängen med den här rapporten, och löftet till användare måste formuleras därefter (sektion 6).

## 2. Vad som kan garanteras — med bevis i koden

| Påstående | Bevis |
|---|---|
| Appen gör **inga nätverksanrop, någonsin** | iOS: inga `URLSession`/`URLRequest`/WebView-anrop i någon fil; inga ATS-nycklar. Android: manifestet saknar `android.permission.INTERNET` — då blockerar operativsystemet varje uppkopplingsförsök från appens process, även från bibliotek. Det är den hårdaste garanti som finns på Android. **iOS har ingen motsvarighet** — alla iOS-appar får använda nätet och det går inte att avsäga sig. Där är garantin i stället: noll nätverkskod, noll beroenden (den enda vägen oväntade anrop brukar smyga in), och kontrollskriptets binärkontroll som visar att den kompilerade appen varken refererar (`nm -u`) eller länkar (`otool -L`) CFNetwork, WebKit eller Network.framework. Användaren kan dessutom själv verifiera det på sin iPhone: Innstillinger → Personvern og sikkerhet → Apprapport om personvern visar varje domän en app kontaktat — för Rusinnsikt står det tomt, för alltid. |
| **Inga tredjepartsbibliotek** som kan ringa hem | iOS: noll Swift-paket, noll Pods (`project.pbxproj` saknar `XCRemoteSwiftPackageReference`). Android: endast AndroidX/Compose/Material från Google, plus `org.json` enbart i enhetstester (`libs.versions.toml`). |
| **Ingen analys, inga annonser, ingen crash-rapportering** | Ingen Firebase/Crashlytics/Sentry/AdMob/etc. i kod eller byggfiler. |
| **Inga identifierare** | Ingen `identifierForVendor`, IDFA, ATT, `ANDROID_ID`, Advertising ID. |
| **Inget konto, ingen inloggning, inga köp** | Stöd sker via `mailto:` som användaren själv skickar (`TipJarView.swift`, `SupportScreen.kt`). Ingen StoreKit/Play Billing. |
| **Inga behörigheter** | Android-manifestet har noll `<uses-permission>`. iOS har inga `NS*UsageDescription`-nycklar och kamera/kontakter/plats är avstängda i projektet (`ENABLE_RESOURCE_ACCESS_* = NO`). |
| **Lagring = exakt en boolean** | `hasSeenOnboarding` i UserDefaults (`DataStore.swift`, `RusFaktaApp.swift`) resp. SharedPreferences (`Models.kt` → `OnboardingState`). Inga favoriter, ingen sökhistorik, inga "senast lästa", ingen cache, ingen databas, inga filer. Kan raderas av användaren under Om → Nullstill velkomstskjerm. |
| **Sökningar sparas inte** | iOS `@State` (bara i minnet). Android `rememberSaveable` (bara i systemets minne för att överleva rotation; skrivs inte till disk och försvinner när appen stängs). |
| **Ingen loggning** | Noll `print`/`NSLog`/`os_log`/`Log.*` i produktionskod. Ingenting hamnar i systemloggar. |
| **Ingen iCloud, ingen molnsynk** | Ingen entitlements-fil alls → ingen iCloud/CloudKit/App Groups/Keychain-delning/push. |
| **Ingen Android-backup av appdata** | `android:allowBackup="false"` + explicita `<exclude>` för alla lagringsdomäner i både `data_extraction_rules.xml` (cloud-backup och device-transfer) och `backup_rules.xml`. |
| **Innehållet exponeras inte för systemet** | Ingen Spotlight-indexering, inga App Intents/Siri/genvägar, inga widgets, inga notiser, inga bakgrundslägen, inga URL-scheman eller deep links. Inga URL:er i innehållet. |
| **Innehållet syns inte i app-växlaren** | iOS: `PrivacyCover` läggs över skärmen så fort appen inte är aktiv (`RusFaktaApp.swift`). Android 13+: `setRecentsScreenshotEnabled(false)` (`MainActivity.kt`). |
| **Privacy manifest och App Store-deklaration stämmer** | `PrivacyInfo.xcprivacy`: `NSPrivacyTracking = false`, inga insamlade datatyper, endast UserDefaults-skälet `CA92.1`. App Store Connect: "No, we do not collect data from this app" är sant. |
| **Personvernerklæringen i appen är sann** | Varje mening i `PrivacyPolicyView.swift`/`PrivacyPolicyScreen.kt` stämmer med koden. |

Allt ovan verifieras automatiskt av `bash scripts/personvern-sjekk.sh` (36 kontroller, alla godkända 2026-09-17).

## 3. Fynd och åtgärder gjorda idag (commit `739f92d`)

Inget av fynden var ett faktiskt dataläckage. Alla är hårdningar eller dokumentfel.

1. **App-växlaren visade sist öppnade stoffside (iOS och Android).** iOS tar ett ögonblicksbild av skärmen när appen läggs i bakgrunden, visar det i app-växlaren och sparar det i appens behållare. Vem som helst som sveper upp app-växlaren på telefonen såg alltså t.ex. "Kokain — tegn på overdose". **Åtgärdat:** `PrivacyCover` (neutral vy med bara ikon + "Rusinnsikt") visas när `scenePhase != .active`. Android 13+: `setRecentsScreenshotEnabled(false)` — döljer bara miniatyren i "Nylige apper". `FLAG_SECURE` valdes medvetet bort eftersom det också skulle blockera skärmdumpar, och användare ska kunna skärmdumpa nödhjälp för att dela.
2. **Android-backupreglerna var tomma mallar.** `allowBackup="false"` fanns, men Googles dokumentation säger uttryckligen att det på Android 12+ "på vissa tillverkares enheter" inte stoppar enhet-till-enhet-överföring, och att ett läge utan regler är "fully enabled for all content". **Åtgärdat:** explicita `<exclude>` för alla domäner i cloud-backup, device-transfer och (för Android ≤11) fullBackupContent.
3. **`CLAUDE.md` i repo-roten beskrev fel projekt.** Filen (otrackad, skapad 2026-09-15 av en annan session) påstod att Rusinnsikt är en "SaaS Web App (React + Node.js)" med Supabase-auth, Stripe-betalningar, JWT, annonser, premium-nivåer och B2B-API — motsatsen till appens grundlöfte. En framtida Claude Code-session som läst den hade kunnat börja bygga in exakt det som inte får finnas. **Åtgärdat:** ersatt med korrekt beskrivning där integritetskraven är absoluta regler, plus hänvisning till kontrollskriptet.
4. **Ny fil `scripts/personvern-sjekk.sh`:** mekanisk kontroll som ska köras före varje release (sektion 7).

Inga ändringar i innehåll, texter eller personvernerklæring behövdes.

## 4. Det appen INTE kan styra — restrisker på plattformsnivå

Det här är sant för varje app som någonsin installerats på en telefon, och det är därför löftet måste formuleras rätt.

**A. Hanterad jobbmobil (MDM) — den viktigaste punkten.**
Microsofts dokumentation för Intune (den vanligaste MDM-lösningen) säger ordagrant: på företagsägda enheter *"they see all installed apps"*; på privata enheter (BYOD) ser organisationen *"the managed app inventory"* men *"some configurations allow organizations to see more than just the managed app inventory"*; på företagsägda Android-enheter **med arbetsprofil** ser de *"only the apps installed in the work profile"*. Apples User Enrollment ger MDM *"only a limited set of payloads and restrictions"*, medan Device Enrollment ger *"more controls and configurations"*.

Praktisk konsekvens:
- **Telefon som arbetsgivaren äger och har registrerat fullt ut (vanlig "jobbmobil"):** arbetsgivarens MDM kan lista alla installerade appar, inklusive Rusinnsikt. Appen kan inte förhindra det — inte med någon kod. Det gäller varje app: Tinder, Vipps, en spelapp.
- **Privat telefon med arbetsprofil (Android) eller User Enrollment (iOS):** appar i den privata delen är osynliga för arbetsgivaren. Rusinnsikt installerad privat syns inte.
- **Nätverksövervakning på jobbet:** helt irrelevant för Rusinnsikt, eftersom appen aldrig gör ett enda nätverksanrop. Det är en verklig, kodbevisad garanti. (Själva nedladdningen från App Store/Play går till Apples/Googles servrar som all annan appnedladdning och avslöjar inte vilken app det var.)

**B. App Store-/Play-kontot.** Installationen finns i köphistoriken hos det Apple-ID/Google-konto som användes. Med privat Apple-ID/Google-konto ser bara användaren själv det.

**C. Telefonens egen backup.** iCloud-backup (privat Apple-ID, krypterad) innehåller listan över installerade appar och appens enda boolean. Googles enhetsbackup innehåller listan över installerade appar även när appdata är avstängt. En arbetsgivare kommer inte åt en privat backup.

**D. Skärmtid / Family Sharing (iOS) och Digital Wellbeing / Family Link (Android).** Den som har Skärmtid-koden eller är familjeorganisatör kan se appanvändning ("Rusinnsikt, 12 min"). Relevant för unga användare med föräldrakontroll. Kan inte påverkas av appen.

**E. Sådant användaren själv gör.** Ringer man 113 hamnar samtalet i samtalsloggen. Delar man en stoffside via iMessage/Messenger finns texten i den appen. Skickar man e-post till info@rusinnsikt.no finns den i Skickat. Allt detta är utanför appen och kräver aktiv handling.

**F. Synlighet på hemskärmen/i App Library, Siri-förslag.** Ikonen och namnet "Rusinnsikt" syns för den som håller i telefonen. (Nu utan att innehållet syns i app-växlaren.)

## 5. Vad som INTE får sägas

- ~~"Ingen kan någonsin se att du har appen"~~ — falskt på en fullt hanterad jobbmobil, och falskt för den som håller i telefonen.
- ~~"1000 % anonym"~~ — inget som körs på en telefon någon annan äger kan lova det.
- ~~"Ingenting sparas"~~ — nästan sant, men det finns en boolean; säg "sparar inget om vad du läst eller sökt".

## 6. Löftet man kan stå för — ordagrant

**Till Martin (svenska), det du kan svära på:**
Appen skickar ingenting någonstans, ringer aldrig hem, har inga spårningsverktyg, inga konton, inga identifierare och sparar inget om vad du läst eller sökt. Det är verifierat i varje kodrad och låst med automatisk kontroll före varje release. Det enda den sparar är om du sett välkomstskärmen, och det kan du radera själv. Om appen ändå syns för någon annan beror det på telefonen (arbetsgivarens MDM på en företagsägd telefon, föräldrakontroll, eller att någon håller i den) — inte på appen.

**Till en användare med jobbmobil (norsk bokmål, klar att klistra in):**
> Rusinnsikt gjør ingen nettverkskall i det hele tatt — Android-versjonen har ikke engang internettilgang, så telefonen nekter appen å koble seg opp. Ingen konto, ingen sporing, ingen analyseverktøy, ingen identifikatorer, og appen lagrer ikke hva du har lest eller søkt på. Innholdet skjules også i app-veksleren. Det eneste som lagres er om du har sett velkomstskjermen, og det kan du slette i appen.
>
> Det appen ikke kan styre, er telefonen selv: hvis arbeidsgiveren din eier telefonen og har den fullt registrert i et administrasjonssystem (MDM), kan de se listen over *alle* installerte apper — det gjelder alle apper, ikke bare denne. Har du en privat telefon med arbeidsprofil, eller en privat telefon uten slikt system, ser de ingenting. Er du usikker på hvordan jobbtelefonen din er satt opp, er det tryggeste å bruke en privat telefon.

## 7. Kontroll före varje release

```
bash scripts/personvern-sjekk.sh
```
Skriptet avslutar med fel om något av de absoluta kraven i `CLAUDE.md` bryts: nätverk, beroenden, identifierare, behörigheter, lagring utöver en boolean, backup-regler, loggning, systemintegrationer, app-växlar-skydd, privacy manifest, identiskt innehåll på båda plattformarna. Kör det i Terminal på Macen **efter** Product → Archive: då kontrolleras även den färdiga iOS-binären (`nm -u`/`otool -L`) — det är iOS-motsvarigheten till Androids saknade INTERNET-behörighet. Lägg aldrig till ett undantag i skriptet utan att först uppdatera den här rapporten.

Manuellt, en gång per release, i App Store Connect: App Privacy = "No, we do not collect data from this app". I Google Play Console (när Android publiceras): Data safety = "No data collected", "No data shared".

## 8. Förslag framåt (inte gjort, kräver beslut)

- **Webbversion på rusinnsikt.no.** För den som inte kan eller vågar installera något alls (fullt hanterad jobbmobil) är en ren webbsida i privat surfläge det enda alternativet som inte lämnar en installerad app efter sig. Innehållet (`Substances.json`) är redan strukturerat för det. Domänen finns. Ett naturligt nästa steg, men en egen leverans.
- **Personvernerklæringen** kan få en mening om app-växlar-skyddet vid nästa textuppdatering. Ingen brådska — den är sann som den är.
