# Rusinnsikt (Vett) — lokal-först-ändringar efter kodgranskningen

**Datum:** 2026-09-06  
**Vem:** Cursor (implementation efter skrivskyddad granskning samma dag)  
**App:** visningsnamn Rusinnsikt, Xcode-projekt `Vett.xcodeproj`, bundle `no.rusinnsikt.app`  
**Granskningen som utgångspunkt:** `Utvecklingsanteckningar/Cursor_kodgranskning_20260906.md` (orörd)

**Vad som INTE gjordes:** crack-postens `shortDescription` i `Vett/RusFaktaApp/Resources/Substances.json` är **oförändrad**. Meningen som beskriver hur fribas tillverkas (“koke kokainpulver med bakepulver eller ammoniakk og vann”) ligger kvar, medvetet, efter Martins krav. `git status` visar clean på den filen. HEAD och arbetskopia har samma text.

**Git-läge för det här arbetet:** ändringarna är **inte committade**. Branch `master` ligger 2 commits före `origin/master` (de två är från 2026-09-05: bundle-ID `no.rusinnsikt.app` och export compliance). Det här passet är working tree: 13 modifierade, 3 raderade, plus nya filer (se filistan nedan).

---

## Bakgrund / varför

Granskningen 2026-09-06 visade att kärnan redan var offline-först (`DataStore` läser bara bundlad `Substances.json`, ingen analytics-SDK). Det som *inte* var lokalt var sidospåren: tip jar, käll-länkar, personvern-länk och App Store-delning.

### Varför tip jar och https-länkar krävde nät

- **StoreKit IAP** (`TipJarStore.loadProducts` / `Product.products(for:)` och `purchase`) går alltid via Apples servrar. Utan nät blir produktlistan tom — samma UI-text som “Støttealternativer er ikke tilgjengelig”. Det går inte att ha *riktiga* in-app-köp och samtidigt vara 100 % offline.
- **`https://`-`Link`** (rusinfo.no, rusopplysningen.no, Claude-artefakt, `apps.apple.com`) öppnar Safari och hämtar sidor. Det är nät även om fakta redan ligger i appen.
- **App Store-delning/recension** använde `AppLinks` med placeholder-ID `0000000000`, alltså en död `https://apps.apple.com/no/app/id0000000000`.

Martins krav efter granskningen: **kärnan måste köras lokalt.** Tip jar och externa https-länkar skulle inte “fixas” genom att byta produkt-ID eller sätta en riktig App Store-URL — de skulle bort, så att fakta, sök och nöd inte förutsätter internet.

### Vad som är kvar som “nät” — efter användaråtgärd, inte i bakgrunden

Appen hämtar inte innehåll, priser eller personvern från internet. Det som *kan* lämna telefonen gör det bara när användaren trycker:

| Åtgärd | Schema | Vad som händer |
|---|---|---|
| Ring nöd / hjälpnummer | `tel:` | Öppnar Telefon-appen. Samtalet går via operatör när användaren ringer. |
| Gi tilbakemelding / Støtt | `mailto:` | Öppnar Mail mot `elofsson.martin@gmail.com`. Ingenting skickas förrän användaren trycker skicka. |
| Del appen / del stoff-sida | `ShareLink` med **vanlig text** | Systemets delningsblad. Ingen App Store-URL, ingen länk-preview mot nät. |

**Utanför appen, vid App Store-inlämning:** Apple Connect-formuläret kräver fortfarande en **publik** Privacy Policy-URL (och Support URL). Det är Apples webbformulär, inte runtime i appen. Ingen sådan URL är inlagd i koden. Texten att publicera finns i `PrivacyPolicyView`.

---

## Git: filer i det här passet

Från `git status` / `git diff` i `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Vett` (inte CoDriverPro).

### Modifierade

- `APP_STORE_PREP.md`
- `README_BUILD_GUIDE.md`
- `Vett.xcodeproj/project.pbxproj` (endast omordning av två Info.plist-nycklar; se per-fil)
- `Vett/RusFaktaApp/Models.swift`
- `Vett/RusFaktaApp/Views/AboutView.swift`
- `Vett/RusFaktaApp/Views/EmergencyView.swift`
- `Vett/RusFaktaApp/Views/OnboardingView.swift`
- `Vett/RusFaktaApp/Views/SubstanceDetailView.swift`
- `Vett/RusFaktaApp/Views/SubstanceListView.swift`
- `Vett/RusFaktaApp/Views/TipJarView.swift`
- `VettTests/SubstanceModelTests.swift`
- `VettTests/SubstancesJSONTests.swift`
- `VettTests/VettTests.swift`

### Raderade (bekräftat borta på disk)

- `Vett/RusFaktaApp/TipJarStore.swift`
- `Vett/RusFaktaApp/AppLinks.swift`
- `Rusinnsikt.storekit`

### Nya (untracked)

- `Vett/RusFaktaApp/Views/PrivacyPolicyView.swift`
- `Utvecklingsanteckningar/Cursor_kodgranskning_20260906.md` (granskningen; skrevs *före* implementationen)
- den här filen: `Utvecklingsanteckningar/Cursor_andringar_lokal_forst_20260906.md`

`NEXT.md` är också untracked (`Fyll i vad som är näst på tur`) och hör **inte** till det här passet.

### Oförändrade (relevanta)

- `Vett/RusFaktaApp/Resources/Substances.json` — inklusive crack-meningen
- `Vett/RusFaktaApp/DataStore.swift` — samma load-från-bundle + `clearAllLocalData()`; UI anropar den nu
- `Vett/RusFaktaApp/ContentView.swift`, `RusFaktaApp.swift`, `FuzzySearch.swift`, `VettTests/FuzzySearchTests.swift`

---

## Granskningsfynd som åtgärdades

Kartläggning mot `Cursor_kodgranskning_20260906.md`.

### P1 — Tip jar-produkt-ID:n tillhör fel bundle

**Granskning:** `TipJarStore` frågade StoreKit efter `com.app.vett.tip.small/medium/large` medan bundle är `no.rusinnsikt.app`. I produktion: tom lista. README påstod fortfarande `com.app.vett`.

**Åtgärd:** IAP togs bort helt, inte byttes ID. Raderat `TipJarStore.swift` och `Rusinnsikt.storekit`. `TipJarView` är nu e-post. README:s donationsavsnitt borta. Kommentarerna om `com.app.vett` finns inte kvar i aktiv kod.

### P1 — “Del appen” / “Vurder i App Store” pekade på id 0000000000

**Granskning:** `AppLinks.appleID = "0000000000"`. ShareLink och recensionslänk + fotnot i `fullPageShareText` öppnade ogiltig App Store-URL.

**Åtgärd:** `AppLinks.swift` raderad. Om-skärmen delar vanlig text. Recensionsraden borta. Stoff-sidans delningstext har ingen `apps.apple.com`-rad. Ingen Apple-ID är införd (fanns inte att läsa i koden).

### P1 — Personvern-URL var en Claude-artefakt

**Granskning:** `AboutView` länkade `https://claude.ai/code/artifact/84a0a41b-…`. Samma URL stod som “PUBLISERT” i `APP_STORE_PREP.md`.

**Åtgärd:** https-länken borta. `NavigationLink` till `PrivacyPolicyView` (text i appen). Prep-dokumentet säger att Connect *fortfarande* behöver en publik URL som Martin äger, och att Claude-artefakter inte ska användas.

### P1 — Om-skärmen påstod saker koden inte höll

**Granskning:** “Ingen internettilkobling kreves” + “Appen lagrer ingenting om deg lokalt”, samtidigt som onboarding-flaggan ligger i UserDefaults och tip jar / https / App Store krävde nät. `clearAllLocalData()` anropades ingenstans.

**Åtgärd:** Copy rättad (fakta/søk/nød utan nät; lokalt sparas bara velkomstflaggan). `Nullstill velkomstskjerm` anropar `store.clearAllLocalData()` och sätter `@AppStorage`-flaggan till `false`. Onboarding-texten nämner samma flagga.

### P1 — Crack-posten beskriver hur fribas tillverkas

**Inte åtgärdat.** Se toppen av dokumentet.

### P2 — `TipJarStore.purchase`: `.unverified` / `finish()`

**Bortfallet.** StoreKit-köpvägen finns inte. Inget att finish:a.

### P2 — Poppers i gruppen “Nikotin og innåndingsmidler”

**Åtgärd:** `SubstanceGroup.forSubstance` mappar `innåndingsmiddel` till `.annet`. Gruppen heter nu “Nikotin” (inte “Nikotin og innåndingsmidler”). Test: `testSubstanceGroup_poppersIsNotNikotin` (poppers → annet, snus + e-sigaretter → nikotin, cannabis → cannabinoider).

### P2 — `ForEach(..., id: \.self)` kraschar vid dubbletter

**Åtgärd:** `SubstanceDetailView` använder `enumerated()` + `id: \.offset` för `overdoseSigns` och bullet-listor (`effects` / risker).

### P2 — `URL(string: "tel:…")!`

**Åtgärd:** `EmergencyNumber.telURL` är `URL?`. Siffror och ledande `+` behålls; mellanslag och annat stryks (`22 59 13 00` → `tel:22591300`). Tomt nummer ger `nil`; knappar blir då icke-länkar i stället för force-unwrap-krasch. 113-rutan på detaljsidan är `if let` kring `URL(string: "tel:113")`.

### P3 — Tom Swift Testing-stubbe i `VettTests.swift`

**Åtgärd:** Filen är XCTest. En metod, `testBundleDoesNotShipPlaceholderAppStoreID`, dokumenterar att App Store-länkar är borta (assertar bara att bundle identifier finns — den *testar inte* frånvaro av `0000000000` i källkod, eftersom `AppLinks` är raderad).

### P3 — `DataStore.clearAllLocalData()` död kod

**Åtgärd:** Knappen under Om. `DataStore` själv är oförändrad.

### P2 kliniskt — mat/dryck till slö men vaken person

**Inte åtgärdat.** Det är JSON-innehåll (`ifConsciousButHeavilySedated`), inte Swift, och låg utanför “fixa allt utom crack”.

### Testluckor från granskningen som *delvis* stängdes

- **113 exakt + giltig `tel:` efter mellanslagsstripp:** `testEmergencyNumbers_areAllPresent` kräver nu `ambulance.number == "113"`, `tel:113`, Giftinformasjonen `tel:22591300`, och `telURL != nil` för alla fyra.
- **Gruppering:** poppers/snus/e-sigaretter/cannabis täcks. **Inte** tillagda: kratom, xylazin, nps (granskningen nämnde dem; ingen ny test).
- **IAP/bundle-assert:** överspelad — produkterna finns inte.

---

## Vad som togs bort

- **Hela IAP-stacken:** `TipJarStore` (`import StoreKit`, `productIDs`, `loadProducts`, `purchase`, transaction-listener, tack-alert).
- **`Rusinnsikt.storekit`:** lokal StoreKit-config med 19/49/99 kr och ID:n `com.app.vett.tip.*` plus intern `_applicationInternalID` `0000000000`.
- **`AppLinks.swift`:** `appleID`, `appStoreURL`, `writeReviewURL`.
- **https-`Link` i UI:** rusinfo.no, rusopplysningen.no, Claude-artefakt, App Store-produktsida, “skriv recension”.
- **App Store-URL i delningstext** från `SubstanceDetailView.fullPageShareText`.
- **README-avsnittet “Donasjoner / tip jar”** (produkt-ID:n, Connect-steg, hur man kopplar storekit-filen till schemat).
- **Påståendet i APP_STORE_PREP** att personvern redan var publicerad på Claude-artefakten och kunde klistras in i Connect.

Källnamnen rusinfo.no / rusopplysningen.no **står kvar som text** (etiketter + caption), men appen öppnar inte sajterna.

---

## Vad som lades till

### `PrivacyPolicyView`

Ny skärm, nås från Om → “Full personvernerklæring”. Ingen nätförfrågan. Innehåll i korthet (norska i UI):

- Senast uppdaterad 6. september 2026.
- Appen är gjord för att fungera utan internet; fakta/nød/søk ligger i appen; ingen konto, ingen analys/reklam-SDK.
- **Hva appen lagrer:** en lokal inställning (velkomstskärm) i UserDefaults; kan rensas via Om → Nullstill.
- **Hva appen ikke gjør:** ingen inloggning, moln, spårning, tredjeparts-statistik-SDK; hämtar inte innehåll när man läser fakta/nød.
- **Telefon og e-post:** `tel:` / `mailto:` öppnar systemappar; själva samtalet/mailet går via operatör/leverantör när användaren skickar/ringer.
- **Kilder:** förkortat från rusinfo.no / Oslo kommune och rusopplysningen.no; appen öppnar inte sajterna.
- **Kontakt:** `elofsson.martin@gmail.com`
- Oberoendefooter (inte tillknyttet myndigheter).

Filen ligger under `Vett/RusFaktaApp/Views/` som synkas via `PBXFileSystemSynchronizedRootGroup` — därför syns den inte som ny explicit fil i `project.pbxproj`.

### Tester (nya/utökade)

- `testTelURL_emptyNumberIsNil`
- Skärpning av `testEmergencyNumbers_areAllPresent` (113, tel-URL:er)
- `testSubstanceGroup_poppersIsNotNikotin`
- `testBundleDoesNotShipPlaceholderAppStoreID` (ersätter tom Swift Testing-stubbe)

### UI-beteende som inte fanns

- Lokal personvernerklæring
- Nullstill velkomstskjerm
- Støtte via Mail i stället för StoreKit-knappar
- Delning utan App Store-länk

---

## Vad som ändrades per fil

### `Vett/RusFaktaApp/TipJarStore.swift` (raderad)

**Syfte:** ta bort all StoreKit.  
**Före:** `@MainActor`-store, tre produkt-ID:n under `com.app.vett`, nätanrop vid init, köp + listener.  
**Efter:** filen finns inte. Inga referenser kvar i Swift-källor.

### `Vett/RusFaktaApp/AppLinks.swift` (raderad)

**Syfte:** ta bort placeholder-App Store-ID och https-URL:er.  
**Före:** `appleID = "0000000000"`, `appStoreURL`, `writeReviewURL`.  
**Efter:** borta. Ingen ersättnings-ID.

### `Rusinnsikt.storekit` (raderad)

**Syfte:** lokal IAP-sim skulle lura att köp fungerar offline.  
**Före:** tre consumables 19/49/99 kr.  
**Efter:** borta.

### `Vett/RusFaktaApp/Views/TipJarView.swift`

**Syfte:** stöd utan IAP.  
**Funktioner/UI:** `supportMailURL` (`mailto:elofsson.martin@gmail.com?subject=Støtte til Rusinnsikt`), en indigo-knapp “Send e-post om støtte”.  
**Före:** `@StateObject TipJarStore`, ProgressView, produktknappar med pris, feltext, alert “Tusen takk!”.  
**Efter:** förklarar att IAP skulle kräva App Store-nät; e-post skickas inte förrän användaren trycker skicka. Ingen `import StoreKit`.

### `Vett/RusFaktaApp/Views/AboutView.swift`

**Syfte:** copy som stämmer, inga https, delning utan App Store, nödnummer via `telURL`, nollställ onboarding.  
**Nytt:** `@AppStorage(OnboardingState.hasSeenOnboardingKey)`, `shareAppText` (vanlig text + “ring 113”), `NavigationLink` till `PrivacyPolicyView` och `TipJarView`, `Nullstill velkomstskjerm`.  
**Före vs efter:**

| Yta | Före | Efter |
|---|---|---|
| Kilder | `Link` till https://rusinfo.no och https://rusopplysningen.no | `Label` (ikon `building.columns`), text att appen inte öppnar sajterna |
| Personvern | “Ingen internettilkobling kreves…”, “lagrer ingenting”, https till Claude | “Fakta, søk og nødhjelp krever ikke internett”, “Lokalt lagres kun om du har sett velkomstskjermen”, intern personvern-sida |
| Støtte-footer | “gratis for alle, uansett” | “Ingen kjøp i appen” |
| Spre | ShareLink med `AppLinks.appStoreURL` + “Vurder … i App Store” | ShareLink med text; recension borta; footer: ingen App Store-lenke, ingen nettforespørsel |
| Hjelpenumre | Text-rader, inte `Link` | `Link` om `telURL` finns, annars text |
| Tilbakemelding | mailto (fanns redan) | samma + footer att Mail öppnas och inget skickas förrän send |
| Nullstill | fanns inte | rensar UserDefaults-nyckeln + sätter AppStorage false |

### `Vett/RusFaktaApp/Views/PrivacyPolicyView.swift` (ny)

Se avsnittet “Vad som lades till”. `section(_:_:)` är en lokal hjälp för rubrik + brödtext.

### `Vett/RusFaktaApp/Views/EmergencyView.swift`

**Syfte:** sluta force-unwrap:a `tel:`-URL:er.  
**Före:** `URL(string: "tel:\(number.number.replacingOccurrences(of: " ", with: ""))")!` på både stora knappar och “Flere hjelpenumre”.  
**Efter:** `callButton` / `allNumbersSection` använder `number.telURL`. Om `nil`: samma rad utan `Link`; VoiceOver-hint “Ring nå” bara när URL finns. `phoneRow` utbruten. Guidance-innehåll oförändrat (kommer från JSON).

### `Vett/RusFaktaApp/Views/OnboardingView.swift`

**Syfte:** samma löfte som Om.  
**Före:** en rad om “ingen konto, ingen sky”.  
**Efter:** extra rad med `wifi.slash` (“Fakta, søk og nødhjelp ligger i appen…”). Lock-raden säger att appen “lagrer bare om du har sett denne skjermen”.

### `Vett/RusFaktaApp/Views/SubstanceDetailView.swift`

**Syfte:** ingen App Store-URL i delning; säkrare list-id; säkrare 113-länk.  
**Före:** sista raden i `fullPageShareText` hade `Last ned appen: \(AppLinks.appStoreURL.absoluteString)`. `ForEach(..., id: \.self)`. `Link(destination: URL(string: "tel:113")!)`.  
**Efter:** avslutning “Fakta og nødhjelp ligger i appen.” utan nedladdnings-URL. `enumerated()` för overdose-tecken och bullet-kort. 113-länk bakom `if let`.

### `Vett/RusFaktaApp/Views/SubstanceListView.swift`

**Syfte:** poppers inte under nikotin.  
**Före:** `nikotinprodukt` **eller** `innåndingsmiddel` → `.nikotin`; titel “Nikotin og innåndingsmidler”.  
**Efter:** bara `nikotinprodukt` → `.nikotin`; `innåndingsmiddel` → `.annet`; titel “Nikotin”. Övrig sök/gruppering orörd.

### `Vett/RusFaktaApp/Models.swift`

**Syfte:** en gemensam, testbar `tel:`-builder.  
**Tillagt på `EmergencyNumber`:**

```swift
var telURL: URL? {
    let digits = number.filter { $0.isNumber || $0 == "+" }
    guard !digits.isEmpty else { return nil }
    return URL(string: "tel:\(digits)")
}
```

Övriga modeller oförändrade.

### `Vett/RusFaktaApp/DataStore.swift`

**Ingen diff.** Laddar `Substances.json` från bundle, `fatalError` om decode misslyckas. `clearAllLocalData()` tar bort `hasSeenOnboarding`. Anropas nu från Om.

### `VettTests/SubstanceModelTests.swift`

Ny: `testTelURL_emptyNumberIsNil`. Befintliga sök/hash-tester orörda.

### `VettTests/SubstancesJSONTests.swift`

`testEmergencyNumbers_areAllPresent` utökad (113 + tel-URL:er). Ny `testSubstanceGroup_poppersIsNotNikotin`. Decode/unika-id/`DataStore()`-testerna orörda.

### `VettTests/VettTests.swift`

Swift Testing-stubbe (`import Testing`, tom `@Test func example`) → XCTest-klass med en dokumenterande test. Kommentar: riktiga host-tester ligger i de tre andra filerna.

### `README_BUILD_GUIDE.md`

Bort: tip jar-ID:n, Connect IAP-steg, storekit-scheme-instruktion, hänvisning till `claude/code-review-2026-08-21.md`.  
Kvar/nytt: schema-namn i guiden står fortfarande som **Vett** (Xcode-schemat som användes vid testkörningen heter **Rusinnsikt** — se Tester). Bundle `no.rusinnsikt.app`. Offline-först-stycke. Connect behöver publik privacy-URL. Pekar på `APP_STORE_PREP.md`.

### `APP_STORE_PREP.md`

Punkt 1: inte “PUBLISERT” på Claude. Full text i appen; Connect kräver ändå URL på sida Martin äger (exempel *som förslag*, inte en existerande URL): `rusinnsikt.no/personvern`.  
Punkt 4: “ingen internettforbindelse kreves” → “Fakta, søk og nødhjelp virker uten internett”; støtte via e-post, inga köp.  
Punkt 5: Support-URL = samma publika sida *när den finns*; tills dess e-post i appen.

Review-noten (engelska, guideline 1.4.3) och 17+-åldersgränsen är **samma utkast** som 2026-09-04, inte omskrivna i det här passet.

### `Vett.xcodeproj/project.pbxproj`

Enda diffen: `INFOPLIST_KEY_CFBundleDisplayName = Rusinnsikt` bytte plats med `INFOPLIST_KEY_ITSAppUsesNonExemptEncryption = NO` i Debug och Release. **Ingen funktionsändring.** Storekit-filen var inte inlagd som explicit pbx-fil (låg löst i repo-roten). Nya Swift-vyer plockas av filesystem-sync.

---

## Tester

### Vad som finns nu i `VettTests`

| Fil | Metoder | Ändrad i det här passet? |
|---|---|---|
| `FuzzySearchTests.swift` | 10 (Levenshtein + `FuzzySearch.matches`) | Nej |
| `SubstanceModelTests.swift` | 5 (söktext, fuzzy-pool, tel tom, Equatable) | Ja, +tel |
| `SubstancesJSONTests.swift` | 7 (decode, icke-tom, unika id, kärnfält, nödnummer, gruppering, DataStore) | Ja, skärpt + gruppering |
| `VettTests.swift` | 1 (bundle identifier finns) | Ja, ombyggd |

### Körning (inte Martins Cmd+U)

I implementationssamtalet 2026-09-06 körde Cursor:

```text
DEVELOPER_DIR=/Users/martin/Downloads/Xcode-beta.app/Contents/Developer
xcodebuild test
  -project …/Vett/Vett.xcodeproj
  -scheme Rusinnsikt
  -destination 'platform=iOS Simulator,name=iPhone 17'
  -only-testing:VettTests
```

Resultat enligt den sessionen: **TEST SUCCEEDED** (exit 0). Första försöket mot schema `Vett` misslyckades för att det delade schemat heter **Rusinnsikt**. Bygget rensade också gamla `AppLinks`/`TipJarStore`-objekt.

**Det här är inte Martins Cmd+U.** Martin har inte rapporterat en egen Xcode-körning. Den här dokumentationssessionen har inte kört om testerna. Påstå inte “bekräftat grönt hos Martin”.

---

## Kvarstående / Apple Connect

1. **Publik Privacy Policy-URL + Support URL** i App Store Connect vid inlämning. Appen visar texten lokalt; Apple vill ha en URL i formuläret. Publicera innehållet från `PrivacyPolicyView` på en sida Martin äger. Använd inte Claude-artefakter. Ingen färdig URL finns i koden.
2. **Crack-meningen** är fortfarande ett App Store **1.4.1 / 1.4.3**-riskmoment (tillagningssteg). Granskningens förslag var omskrivning utan metod; det är inte gjort.
3. **Klinisk P2** (“Hvis personen er våken, men svært sløv” → mat/drikke, aspirationsrisk) orörd i JSON.
4. **Grupperingstester** saknas fortfarande för kratom / xylazin / nps.
5. **`ENABLE_OUTGOING_NETWORK_CONNECTIONS = NO`** i pbxproj (Mac/Catalyst-mall) rördes inte. iOS-target är `1,2`. Relevant bara om Catalyst slås på senare.
6. **Ingen commit** av det här passet. Working tree är smutsigt.

### Smoke på fysisk iPhone (Martin)

Simulator ringer inte och skickar inte mail. Värt att trycka igenom:

- Onboarding → “Jeg forstår, fortsett” → flikarna Fakta / Nødhjelp / Søk / Om
- Nødhjelp: 113 och Giftinformasjonen (`22 59 13 00` → ska bli `tel:22591300`)
- Om: hjelpenumre, Gi tilbakemelding, Støtt → Mail, Del appen (text, ingen App Store-länk)
- Om → Full personvernerklæring (ingen Safari)
- Om → Nullstill velkomstskjerm → appen ska visa onboarding igen
- En stoff-sida → Del info (text utan `apps.apple.com`)
- VoiceOver på de röda nödknapparna (“Ring nå”)
- Flygplansläge: fakta, sök och nöd ska fortfarande öppnas (tel/mailto kan ändå öppna systemappar)

---

## Hur appen beter sig nu för användaren

Flikarna är oförändrade: **Fakta**, **Nødhjelp**, **Søk**, **Om** (`ContentView`).

### Onboarding (första start)

Visas när `hasSeenOnboarding` är false (`RusFaktaApp` + `@AppStorage`). Texten säger att det är ett nøytralt faktaoppslagsverk, källor som text, skadereduksjon, att fakta/søk/nød ligger i appen utan internet, att enda lokala lagringen är att skärmen setts, 17+. Knapp: “Jeg forstår, fortsett”.

### Fakta

Kategori-grupperad lista från bundlad JSON (ca 27 poster). Sök är lokalt (fuzzy mot namn/alias/kategori + “overdose”/“nødhjelp”). Footer: källnamn som text, “Søk lagres aldri og sendes aldri noe sted.” Poppers ligger under **Andre stoffer**, inte Nikotin. Detaljsida: kort om stoffet, virkning, risk, overdose-kort med 113, blandning, juridik, en delningsknapp med **hela sidans text** och utan App Store-länk. Sök med overdose-ord scrollar till overdose-kortet (samma som före).

### Nød

Egen flik, röd gyllene regel, två stora anropsknappar (ambulanse 113, Giftinformasjonen), vägledning, sedan “Flere hjelpenumre” (RUSinfo `915 08 588`, Pårørende `800 40 567`). Tryck öppnar Telefon via `tel:` med strippad siffra. Ingen sök, inget StoreKit.

### Om

Ingress, källor som text, personvern-punkter + länk till intern erklæring, valgfri støtte (ingen IAP), delning som text, klikkbare hjelpenumre, tilbakemelding via Mail, nullstill velkomst, ansvarsfraskrivelse, “ikke tilknyttet myndigheter”.

### Støtte

Bara från Om. Ingen paywall på fakta/nød. Ingen prisknapp. En e-postknapp. Appen förblir gratis.

### Delning

- App: kort norsk text + “Ved mistanke om overdose: ring 113.”
- Stoff: hela sidans fakta som punktlistor + “Ring 113” + “Delt fra Rusinnsikt-appen …” utan nedladdnings-URL.

### Personvern

I appen. Ingen Claude-länk. Connect-fältet är fortfarande Martins uppgift före inlämning.

---

## Senaste git-log (före det här passet)

```
3d8bcae 2026-09-05 Declare no non-exempt encryption for App Store export compliance
108a463 2026-09-05 Change bundle identifier to no.rusinnsikt.app before App Store Connect
4739e39 2026-09-04 Restyle substance list rows as cards to match detail page
c3ee598 2026-09-04 Publish privacy/support page, link it from the app and prep doc
ccfc26b 2026-09-04 Add App Store release prep: privacy manifest + listing draft
5e857c6 2026-09-04 New app icon: R monogram
2c10ec8 2026-09-04 Set app Display Name to Rusinnsikt
d458af8 2026-09-04 Rename app content from Vett to Rusinnsikt
…
ac14fda 2026-08-21 Initial commit
```

Lokal-först-passet 2026-09-06 har **inget eget commit**.
