---
name: app-store-connect-guide
description: Steg-för-steg-guide (engelska fältnamn, exakt som i App Store Connects UI) för var varje textbit/inställning från APP_STORE_PREP.md ska fyllas i vid inlämning av Rusinnsikt. Läs tillsammans med APP_STORE_PREP.md som har själva texterna.
sources: [cowork]
---

Denna guide pekar exakt på VAR i App Store Connect (engelskt gränssnitt) varje sak
från `APP_STORE_PREP.md` ska fyllas i. Fältnamnen nedan är Apples egna, verifierade
mot developer.apple.com/help/app-store-connect (september 2026).

Gå igenom i denna ordning: **1) App Information → 2) Pricing and Availability →
3) App Privacy → 4) versionssidan (Screenshots, text, build) → 5) Submit.**

---

## 1. App Information (vänstermenyn, under "General")

Klicka på appen i Apps-listan, sedan **App Information** i vänstermenyn.

| Fält i App Store Connect | Vad du fyller i | Källa |
|---|---|---|
| **Subtitle** (max 30 tecken) | `Fakta om rusmidler og hjelp` | APP_STORE_PREP.md punkt 4 |
| **Privacy Policy URL** | `https://devlyn.no/rusinnsikt/personvern/` (så snart sidan är publicerad — tills dess Google Sites-adressen) | APP_STORE_PREP.md punkt 1 |
| **Category** → Primary Category | **Medical** (redan satt i appens projektfil, men kontrollera att den syns här också) | — |
| **Category** → Secondary Category | Valfritt, kan lämnas tomt, eller t.ex. **Reference** | — |
| **Content Rights** | Välj **"No, it does not contain, show, or access third-party content"** (appen innehåller ingen tredjepartskontent — texterna är omskrivna/sammanfattade, inga citat eller bilder från andra) | — |
| **Age Rating** | Se separat sektion nedan — knappen heter **"Set Up Age Ratings"**/"Edit" i denna sektion | APP_STORE_PREP.md punkt 3 |
| **License Agreement** | Lämna standard (Apples standardavtal) om du inte vill något annat | — |

### Age Rating — så fyller du i den

I App Information-sidan, hitta Age Ratings-sektionen och klicka **"Set Up Age Ratings"**
(eller "Edit" om den redan är påbörjad).

1. Första steget frågar om in-app-funktioner (inköp, användargenererat innehåll osv.) — svara **Nej/None** på allt, appen har inget av detta.
2. Klicka **Next** genom sektionerna tills du når frågan om innehållstyper. Leta efter:
   **"Alcohol, Tobacco, or Drug Use or References"**
   → Svara ärligt utifrån att HELA appen handlar om fakta om rusmidler: välj **Frequent/Intense** (inte None eller Infrequent/Mild — appens kärninnehåll är just detta, så det vore osant att tona ner det).
3. Alla andra innehållskategorier (våld, sex, språk osv.) → **None**, det finns inget sådant i appen.
4. Sista steget ("Additional Information") visar den beräknade åldersgränsen — den bör bli **17+**. Det är korrekt och förväntat, inte ett fel (se APP_STORE_PREP.md punkt 3 för varför).
5. Klicka **Save**.

---

## 2. Pricing and Availability (egen flik i vänstermenyn)

- **Price** → välj **Free** (Prisnivå 0).
- **Availability** → lämna som standard (alla länder/regioner), om du inte specifikt vill begränsa till Norge.

---

## 3. App Privacy (egen flik i vänstermenyn, under "General")

1. Klicka **App Privacy** i vänstermenyn.
2. Klicka **Get Started** (om det är första gången).
3. Frågan som kommer: *"Indicate whether you or your third-party partners collect data from your app."*
   → Svara **"No, we do not collect data from this app."**
4. Klicka **Save**.
5. Inget mer behövs — appen har ingen tredjepartskod och samlar ingen data (bekräftat av `PrivacyInfo.xcprivacy` i koden).
6. Kom ihåg att klicka **Publish** på denna sida när du är redo (annars syns inte svaret på produktsidan).

---

## 4. Versionssidan (klicka på versionsnumret, t.ex. "1.0 Prepare for Submission")

Detta är sidan där själva app-store-sidan för just denna version fylls i.

### Screenshots
- Ladda upp de 6 bilderna från `App Store Screenshots/`-mappen under **iPhone 6.5" Display** (eller motsvarande storlek som visas — bilderna är 1284×2778 px, vilket är just den storleken).
- Ordning spelar roll — dra dem i samma ordning som filnamnen (01–06) så onboarding/fakta/nødhjelp/sök/om visas i logisk ordning.

### Textfält (i den ordning de visas på sidan)

| Fält | Text | Källa |
|---|---|---|
| **Promotional Text** (max 170 tecken, kan ändras senare utan ny granskning) | *"Nøytral fakta om rusmidler og risiko — uten internett, uten konto, uten sporing. Nødnumre og overdoseveiledning alltid tilgjengelig."* | APP_STORE_PREP.md punkt 4 |
| **Description** (max 4000 tecken) | Hela beskrivningstexten under "Description" i APP_STORE_PREP.md punkt 4 (den långa texten med FAKTA OM RUSMIDLER / NØDHJELP NÅR DET HASTER / SØK RASKT / PERSONVERN SOM STANDARD / HELT GRATIS) | APP_STORE_PREP.md punkt 4 |
| **Keywords** (max 100 tecken, kommaseparerat) | `rus,narkotika,rusmidler,skadereduksjon,overdose,nødhjelp,giftinformasjon,fakta,helse,ungdom` | APP_STORE_PREP.md punkt 4 |
| **Support URL** | `https://devlyn.no/rusinnsikt/` | APP_STORE_PREP.md punkt 5 |
| **Marketing URL** (valfritt) | Lämna tomt | APP_STORE_PREP.md punkt 5 |

### Build
- Scrolla ner till **Build**-sektionen, klicka **"+"** eller **"Select a build before you submit your app"**.
- Välj den senaste processade builden (per 2026-09-25: **1.0 (6)** i TestFlight; nästa arkiv får build 7 och heter "Rusinnsikt 1.0 (7)" efter namnbytet från Vett). Builden måste ha hunnit bli processad av Apple (10–30 min efter uppladdning) innan den syns i listan.

### General App Information (längre ner på samma sida eller egen ruta)
| Fält | Värde |
|---|---|
| **Version** | 1.0 (fylls redan i automatiskt utifrån MARKETING_VERSION) |
| **Copyright** | `2026 Devlyn IT` |

### App Review Information (längst ner på versionssidan)
| Fält | Värde |
|---|---|
| **Contact Information → First/Last Name** | Ditt namn (Apple kräver en fysisk kontaktperson här — det syns inte offentligt, bara för Apples granskare) |
| **Contact Information → Phone Number** | Ditt telefonnummer |
| **Contact Information → Email** | info@rusinnsikt.no |
| **Sign-in required** | **No** (appen kräver ingen inloggning) |
| **Notes** | Klistra in hela reviewer-notatet från APP_STORE_PREP.md punkt 2 (den engelska texten som börjar "Rusinnsikt is a Norwegian, non-commercial...") |

### Version Release
- Välj **"Automatically release this version"** om du vill att appen ska bli tillgänglig direkt när Apple godkänner den, eller **"Manually release this version"** om du vill trycka på en extra knapp själv efteråt.

---

## 5. Submit for Review

Längst upp eller längst ner på versionssidan finns knappen **"Add for Review"** eller **"Submit for Review"** (namnet kan variera något beroende på Apples aktuella UI-version).

Innan den går igenom kan Apple fråga:
- **Export Compliance** → redan förifyllt som "No" tack vare `ITSAppUsesNonExemptEncryption = NO` i projektet, men bekräfta om den frågar igen.
- **Content Rights** → samma svar som du gav i App Information (No third-party content).
- **Advertising Identifier (IDFA)** → svara **"No, this app does not use the Advertising Identifier"** — appen har ingen reklam eller spårning.

Klicka igenom, och sen är det inskickat. 🎉 (kan ta allt från några timmar till några dagar innan Apple svarar)

---

*Uppdaterad 2026-09-25: support- och personvernsidorna flyttas från Google Sites till devlyn.no/rusinnsikt/ (källfiler i repot under `docs/devlyn-site/rusinnsikt/`). Utvecklar-/utgivarnamnet är **Devlyn IT** (copyright `2026 Devlyn IT`; Om-skärmen i appen säger "Rusinnsikt er utviklet av Devlyn IT"). Kontaktmail är info@rusinnsikt.no. Xcode-projektet heter `Rusinnsikt.xcodeproj` och arkiv/app-bundle heter Rusinnsikt; bundle-id `no.rusinnsikt.app` är oförändrat, så inget behöver ändras i App Store Connect. Det enda stället där Martins namn fortfarande syns offentligt är Seller-namnet i App Store, som visar Apple-kontoinnehavaren tills ett Organization-konto finns (se APP_STORE_PREP.md punkt 10).*
