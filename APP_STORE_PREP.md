# Rusinnsikt — forberedelser for App Store-lansering

Utkast forberedt av Claude 2026-09-04. Alt innhold nedenfor er utkast — les
igjennom og juster før du bruker det.

## 1. Personvernerklæring (Privacy Policy)

Full tekst ligger **i appen** (Om → Full personvernerklæring). Appen henter
ikke personvern fra nett.

**App Store Connect** krever likevel en offentlig URL i feltene Privacy
Policy URL og Support URL. HTML-kilden ligger i `docs/privacy.html` (samme
innhold som `PrivacyPolicyView`). Publiser den på en side du eier **før**
innsending, f.eks. Google Sites:

`https://sites.google.com/view/rusinnsikt-personvern/`

Ikke bruk Claude-artefakter — de kan være private eller forsvinne.

---

## 2. Merknad til Apples App Review-team

Apples retningslinje 1.4.3 forbyr apper som "oppfordrer til" bruk av
ulovlige rusmidler. Rusinnsikt gjør det motsatte — det er derfor viktig å
forklare formålet tydelig for reviewer, siden emnet alene kan trigge ekstra
gransking. Lim inn noe slikt i App Store Connect → App Review Information →
Notes:

> Rusinnsikt is a Norwegian, non-commercial, factual harm-reduction
> reference app. It summarizes and shortens publicly available content
> from two official Norwegian public-health resources — rusinfo.no (Oslo
> municipality) and rusopplysningen.no — to help young people and others
> find accurate, neutral information quickly, including emergency numbers
> and overdose guidance. The app explicitly does not encourage or promote
> substance use (see the in-app disclaimer and "Om" screen), does not
> facilitate the purchase or sale of any substance, does not include user
> accounts or social features, and does not collect any user data. It is
> modeled on the same harm-reduction/prevention approach used by Norwegian
> public health authorities. Age rating reflects the informational content
> about drugs, consistent with reference apps in this category.

## 3. Aldersgrense (Age Rating)

App Store Connects spørreskjema for aldersgrense inkluderer et spørsmål om
"Referanser til ulovlig narkotikabruk". Ærlig svar der vil trolig gi appen
**17+** — det er forventet og normalt for denne typen faktainnhold, ikke et
tegn på at noe er feil. Ikke svar "Nei" for å unngå det; det er noe Apple
kan avvise appen for i etterkant hvis det oppdages.

## 4. Tekst til App Store-oppføringen

**Undertittel (30 tegn maks):** Fakta om rusmidler og hjelp

**Nøkkelord (kommaseparert, 100 tegn maks):**
rus,narkotika,rusmidler,skadereduksjon,overdose,nødhjelp,giftinformasjon,fakta,helse,ungdom

**Promotional Text (170 tegn maks — kan endres når som helst uten ny app-gjennomgang):**

Nøytral fakta om rusmidler og risiko — uten internett, uten konto, uten sporing. Nødnumre og overdoseveiledning alltid tilgjengelig.

**Description (4000 tegn maks):**

Rusinnsikt er et nøytralt, faktabasert oppslagsverk om rusmidler og risiko — laget for å gi ungdom og andre lettforståelig og korrekt informasjon. Appen oppfordrer ikke til bruk. Målet er å forebygge skade og gjøre det raskere å søke hjelp hvis noe går galt.

FAKTA OM RUSMIDLER
Kortfattede sammendrag om virkning, risiko og skadereduksjon for et bredt utvalg stoffer — fra sentralstimulerende og opioider til cannabinoider, nikotin og andre stoffer. Innholdet er skrevet om og forkortet fra rusinfo.no (Oslo kommune) og rusopplysningen.no, og ligger ferdig i appen.

NØDHJELP NÅR DET HASTER
Ett trykk unna: nødnummer 113 og Giftinformasjonen (døgnåpen, 22 59 13 00). Tydelig, trinnvis veiledning for tre situasjoner — bevisstløs, våken men svært sløv, eller urolig/hallusinerende — pluss råd om etterpå og om å blande stoffer.

SØK RASKT
Søk etter stoff eller stikkord for å finne informasjonen du trenger med én gang.

PERSONVERN SOM STANDARD
• Ingen konto, ingen innlogging
• Fakta, søk og nødhjelp krever ikke internett
• Ingen analyse- eller sporingsverktøy
• Lokalt lagres kun om du har sett velkomstskjermen — ingenting annet

HELT GRATIS
Ingen kjøp i appen. Støtte er helt frivillig.

Rusinnsikt er et uavhengig informasjonsprosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.

Ved mistanke om overdose eller forgiftning: ring alltid 113 eller Giftinformasjonen, uavhengig av hva som står i appen. Rusinnsikt erstatter ikke profesjonell medisinsk vurdering, akutthjelp eller rådgivning.

## 5. Support-URL og Marketing URL

**Support URL (obligatorisk).** Samme blokkering som personvernerklæringen
i punkt 1 — du trenger én offentlig side du selv eier.

Ferdig tekst til siden ligger i `docs/support-privacy-tekst.txt` (ren
tekst — Support-seksjon øverst, personvernerklæring under, klar til å
lime inn i Google Sites eller tilsvarende). Publiser den, og bruk samme
URL i både Support URL- og Privacy Policy URL-feltet i App Store Connect,
f.eks.:

`https://sites.google.com/view/rusinnsikt-personvern/`

Ikke bruk Claude-artefakter — de kan være private eller forsvinne.

**Marketing URL (valgfritt).** Ikke påkrevd. La stå tomt, eller bruk samme
side som over hvis du vil ha én lenke som "peker på appen" i søkeresultater
og annonsering. Ingen egen landingsside er nødvendig for lansering.

## 6. Copyright

**Copyright-feltet i App Store Connect (under App Information):**

`2026 Martin Elofsson`

(Formatet Apple forventer er år + rettighetshaver, uten "©" — Apple legger
selv til symbolet i visningen.)

---

## 7. Status per 2026-09-12 — teknisk klargjøring gjort av Claude

Gjennomgått og bekreftet/rettet før innsending:

- **Personvern/support-side**: `https://sites.google.com/view/rusinnsikt-personvern/` er **live og publisert** (bekreftet). Inneholder personvernerklæring + kontakt-e-post (elofsson.martin@gmail.com). Kan brukes som both Privacy Policy URL og Support URL i App Store Connect, slik punkt 1/5 sier.
- **Skjermdumper**: 6 stk, 1284×2778 px (iPhone 6.5"-klassen) i `App Store Screenshots/`. Bekreftet gyldig og tilstrekkelig format mot Apples offisielle screenshot-spesifikasjon (developer.apple.com) — ingen flere størrelser er påkrevd for innsending.
- **App-ikon**: 1024×1024, RGB uten alpha-kanal — korrekt for App Store-markedsføringsikonet.
- **Eksportoverholdelse**: `ITSAppUsesNonExemptEncryption = NO` er satt i prosjektet — matcher "ingen ikke-unntatt kryptering"-svaret i App Store Connect.
- **Personvernmanifest** (`PrivacyInfo.xcprivacy`): til stede og korrekt — ingen datainnsamling deklarert, `NSPrivacyTracking = false`, UserDefaults-bruk deklarert med gyldig årsakskode (CA92.1). Ingen tredjeparts SPM-avhengigheter i prosjektet som ville krevd egne manifester.
- **Kategori**: `public.app-category.medical` er allerede satt som primærkategori.
- **RETTET: iPad-støtte fjernet.** Prosjektet hadde `TARGETED_DEVICE_FAMILY = "1,2"` (Universal, altså også iPad), men appen er aldri testet eller skjermbildet på iPad, og Apple **krever iPad-skjermdumper (13"/12.9") for alle Universal-apper** — uten dem hadde innsendingen blitt blokkert. Satt til iPhone-only (`TARGETED_DEVICE_FAMILY = 1`) i alle 4 build-konfigurasjonene. iPad-støtte kan legges til i en senere versjon når layouten faktisk er testet der.
- **Build-nummer bumpet**: `CURRENT_PROJECT_VERSION` for hovedmålet (no.rusinnsikt.app) satt fra 1 → **2**, siden iPhone-only-endringen over må inn i en ny arkivert build (den forrige TestFlight-opplastingen ble gjort med Universal-innstillingen). `MARKETING_VERSION` er fortsatt 1.0.

**Alt dette er committet i git.** Det som gjenstår er de stegene som krever din Apple-innlogging — se punkt 8.

## 8. Din gjenstående sjekkliste (Apple-kontospesifikt, kan ikke gjøres herfra)

1. **Arkiver på nytt i Xcode** (Product → Archive) nå som iPhone-only + build 2 er satt — den gamle TestFlight-opplastingen (build 1, Universal) bør ikke brukes til innsendingen.
2. Last opp den nye arkiverte builden til App Store Connect (Distribute App → App Store Connect).
3. I App Store Connect, opprett versjon **1.0** for appen (hvis den ikke allerede finnes) og velg **build 2** når den har blitt behandlet ferdig (tar vanligvis 10–30 min).
4. Lim inn tekstene fra punkt 4 (Undertittel, Nøkkelord, Promotional Text, Description) og punkt 6 (Copyright).
5. Fyll inn **Privacy Policy URL** og **Support URL** = `https://sites.google.com/view/rusinnsikt-personvern/` (punkt 1/5).
6. **App Privacy (personvern-"næringsetikett")**: Siden appen ikke samler inn noen data, svar **"Data Not Collected"** på hele spørreskjemaet.
7. **Age Rating**: svar ærlig på spørsmålet om referanser til narkotikabruk — forvent 17+ (punkt 3).
8. Lim inn App Review-notatet fra punkt 2 under App Review Information → Notes, og fyll i din e-post/telefon som kontakt der.
9. Sett pris til Gratis, velg tilgjengelige land/regioner.
10. Trykk **Submit for Review**.
