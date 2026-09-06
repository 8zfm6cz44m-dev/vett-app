---
name: Cursor kodgranskning Rusinnsikt/Vett 2026-09-06
description: Skrivskyddad granskning av aktiv Swift-kod i Vett (Rusinnsikt). Ingen produktionskod ändrad. Tester inte körda.
---

# Rusinnsikt (Vett) — oberoende kodgranskning (Cursor, 2026-09-06)

**Vad det är:** iOS-app, visningsnamn Rusinnsikt, bundle `no.rusinnsikt.app`, kategori Medical, iOS 17.6+. Harm-reduction-oppslagsverk med bundlad `Substances.json` (~27 stoffer), nödflik, fuzzy-søk, valgfri tip jar. Ingen konto, ingen analytics-SDK.

**Metod:** läst `Vett/RusFaktaApp/**`, `VettTests/**`, `Substances.json` (struktur + stickprov, inte medicinsk faktagranskning av varje post), `project.pbxproj`, `APP_STORE_PREP.md`, `README_BUILD_GUIDE.md`. `Arkiv/` finns inte. Ingen Cmd+U.

---

## Sammanfattning

Kärnan är liten, offline-först och medvetet byggd för App Review (onboarding, disclaimer, 113 först). Sök + overdose-scroll är genomtänkta och testade. Det som kan sabba **release** är inte parsern utan **identitet och butik**: IAP-produkt-ID:n matchar inte bundle ID, App Store-länken är placeholder, personvern-URL ligger på en Claude-artefakt, och Om-texten motsäger UserDefaults + nätverkslänkar.

Inga P0 som kraschar faktasidorna vid normal start, givet att JSON fortsätter decoda (testerna täcker det).

---

## 1. Bekräftade buggar / release-fel

### P1 — Tip jar-produkt-ID:n tillhör fel bundle

- **Fil:** `TipJarStore.swift` ~23–27, `Rusinnsikt.storekit`, `README_BUILD_GUIDE.md` (~28). **Target:** `PRODUCT_BUNDLE_IDENTIFIER = no.rusinnsikt.app` (`project.pbxproj` ~356).
- **Utgångsläge:** Appen frågar StoreKit efter `com.app.vett.tip.small/medium/large`. Kommentar och README säger bundle `com.app.vett`.
- **Vad som blir fel:** I produktion hämtar `Product.products(for:)` tom lista (samma som “Støttealternativer er ikke tilgjengelig”). Köp går inte igenom. Lokal StoreKit-config kan lura att det fungerar i Simulator.
- **Varför:** IAP-ID:n måste ligga under appens riktiga bundle (vanligtvis `no.rusinnsikt.app.tip.small` o.s.v.) och skapas i Connect mot **samma** app-post.
- **Test som borde fånga det:** Statisk assert att varje `TipJarStore.productIDs` har prefix `Bundle.main.bundleIdentifier` (eller en konstant som är samma som `PRODUCT_BUNDLE_IDENTIFIER`).

### P1 — “Del appen” / “Vurder i App Store” pekar på id 0000000000

- **Fil:** `AppLinks.swift` ~14–24. Används i `AboutView` ShareLink + review-link, och i `SubstanceDetailView.fullPageShareText`.
- **Utgångsläge:** `appleID = "0000000000"` med TODO.
- **Vad som blir fel:** Alla delningar och “skriv recension” öppnar en ogiltig App Store-URL. Mottagare av en överdos-sida får en död nedladdningslänk.
- **Test:** `XCTAssertFalse(AppLinks.appleID == "0000000000")` som release-gate, eller skippa tills ID finns.

### P1 — Personvern-URL är en Claude-artefakt

- **Fil:** `AboutView.swift` ~47–48. Dokumenterat i `APP_STORE_PREP.md` (~7–13).
- **Utgångsläge:** `https://claude.ai/code/artifact/84a0a41b-…`
- **Vad som blir fel:** Apple Review + användare behöver en **publik, stabil** sida. Artefakter är privata tills de delas, kan försvinna, och ser inte ut som en officiell policy. Connect-fälten Privacy Policy URL / Support URL med samma länk kan avvisas.
- **Varför:** Inte appkrasch, men release-blocker enligt Apples egna krav på privacy URL.

### P1 — Om-skärmen påstår saker koden inte håller

- **Fil:** `AboutView` Personvern-sektion ~42–46. Jämför `DataStore` / `OnboardingState.hasSeenOnboardingKey`, `PrivacyInfo.xcprivacy` (UserDefaults CA92.1), `TipJarStore.loadProducts`, `Link` till rusinfo/rusopplysningen, mailto, App Store.
- **Utgångsläge:** “Ingen internettilkobling kreves”, “Appen lagrer ingenting om deg lokalt”.
- **Vad som blir fel:** Onboarding-flaggan **är** lokal lagring. Tip jar, källor, personvern, delning och recension **kräver** nät (eller failar tyst). App Privacy-nutrition och Om-texten kan ses som vilseledande.
- **Varför:** `clearAllLocalData()` finns men anropas ingenstans i UI.
- **Test:** Inte kodfel i striktest mening; copy måste matcha `UserDefaults` + StoreKit.

### P1 — Crack-posten beskriver hur fribas tillverkas

- **Fil:** `Substances.json` `id: crack`, `shortDescription` (~89): koka kokainpulver med bakpulver/ammoniak och vatten.
- **Utgångsläge:** Harm-reduction-fakta, men formuleringen är ett **tillagningsrecept**.
- **Vad som blir fel:** App Store 1.4.1/1.4.3 (illegal drugs; inte uppmuntra/facilitera). Reviewern kan tolka det som tillverkningsanvisning även om resten av appen är skadereduktion. Resten av JSON:en (stickprov) är risk/öd/blandning, inte dosrecept — den här meningen sticker ut.
- **Test:** Inget. Innehållsgranskning / jurist / omskriv till “rökbar form av kokain, högre beroende- och lungrisk” utan metod.

---

## 2. Viktiga testluckor

- **113 exakt:** `testEmergencyNumbers_areAllPresent` kräver fyra icke-tomma nummer, inte att ambulans **är** `"113"` eller att `tel:`-URL:en är giltig efter mellanslagsstripp.
- **Gruppering:** `SubstanceGroup.forSubstance` har specialfall `cannabis` och keyword-ordning. Ingen test att kratom (`opioid` i kategoristrängen) landar under opioider, poppers under innånding, xylazin/nps under annet.
- **IAP/bundle** — se P1.
- **`VettTests.swift`:** tom Swift Testing-stubbe. Riktiga tester är XCTest i tre andra filer. Förvirrande, ingen täckning.
- **StoreKit:** `purchase` ignorerar `.unverified`; transaktionen `finish()`:as inte. Ingen test. Kan lämna hängande transaktioner (P2 i produktion).
- Medicinsk sanning i 27 poster är **inte** testbar i kod. Bara tomma fält och unika `id`.

JSON-sviten (decode, icke-tom, unika id, overdoseSigns/emergencyAction ifyllda, `DataStore()` kraschar inte) är rätt ställd mot `fatalError` i `DataStore.init`.

---

## 3. Saker som kräver fysisk iPhone / Review

- `tel:113` och Giftinformasjonen `tel:22591300` (mellanslag tas bort i `EmergencyView`). Simulator ringer inte.
- Tip jar mot **Connect-produkter med rätt bundle**, inte bara `Rusinnsikt.storekit`.
- VoiceOver på nödknapparna (redan `accessibilityElement` + “Ring nå”) — bra kandidat för manuell runda.
- App Review 17+ / guideline-noten i `APP_STORE_PREP.md` — produkt, inte kod.
- `ENABLE_OUTGOING_NETWORK_CONNECTIONS = NO` + App Sandbox i pbxproj: typiskt Xcode-mall för Mac. iOS-target `1,2`. Om Catalyst/Mac slås på senare kan länkar och StoreKit dö.

---

## 4. Delar som verifierats som korrekta (kod, inte kört)

- **Offline kärna:** `DataStore` läser bara bundle-JSON. Ingen analytics-SDK. `PrivacyInfo.xcprivacy`: tracking false, inga collected types, UserDefaults CA92.1. Mappen synkas in via `PBXFileSystemSynchronizedRootGroup`.
- **Nød:** egen flik, 113 + Giftinformasjonen överst, röd gyllene regel. Detaljsida: röd 113-ruta + länk till full guide. Sök med overdose-ord sätter `scrollToOverdose` (samma `FuzzySearch` som listan).
- **Sök:** Damerau-Levenshtein + prefix; fuzzy bara mot namn/alias/kategori + injicerade “overdose”/“nødhjelp”; lång brödtext bara exact substring. Tester i `FuzzySearchTests` / `SubstanceModelTests` låser det.
- **Tip jar gated:** bara från Om, ingen paywall på fakta/nød.
- **Disclaimer + källor + “ikke tilknyttet myndigheter”** finns i Om och onboarding.
- **Swift 6:** modeller `nonisolated`; `TipJarStore` dokumenterar MainActor/transaction-listener-mönstret.
- **Innehållsmodell:** 27 unika `id` i JSON (alkohol … zolpidem). Tester kräver unika id.

Kända medvetna avvägningar: två flikar med `SubstanceListView` (Fakta vs Søk med autofokus) — separat sökstate. `fatalError` om JSON saknas — fail-closed, fångas av tester om host app är Vett.

---

## 5. Mindre (P2/P3)

- **P2** `TipJarStore.purchase`: `.unverified` gör inget; bara `.verified` finish + tack.
- **P2** Poppers (`Innåndingsmiddel`) hamnar i gruppen “Nikotin og innåndingsmidler” — korrekt mot keyword, förvirrande i UI.
- **P2** `ForEach(overdoseSigns, id: \.self)` kraschar om två identiska strängar i samma lista.
- **P2** `URL(string: "tel:…")!` — tomt nummer är testat; ugyldig sträng är inte det.
- **P3** Tom `VettTests.swift` (Swift Testing).
- **P3** `DataStore.clearAllLocalData()` död kod.
- **P2 kliniskt (inte compiler):** “Hvis personen er våken, men svært sløv” → ge mat/drikke. Aspirationsrisk. Innehåll, inte Swift.

---

## 6. Innan App Store (kort)

1. Lås bundle ID `no.rusinnsikt.app` mot IAP-ID:n och StoreKit-filen.  
2. Sätt riktigt Apple ID i `AppLinks`.  
3. Flytta personvern/support till en URL du äger; gör inte Claude-artefakt till Connect-fält.  
4. Skriv om crack-meningen utan tillagningssteg.  
5. Rätta Om-copy (onboarding-flagga + “nett trengs för länkar/tips”).  
6. Cmd+U på VettTests med host app Vett.

Jag har inte kört testerna och inte bedömt om varje JSON-post medicinskt stämmer mot rusinfo.no.
