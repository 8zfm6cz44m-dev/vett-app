# Rusinnsikt — forberedelser for App Store-lansering

Utkast forberedt av Claude 2026-09-04. Alt innhold nedenfor er utkast — les
igjennom og juster før du bruker det.

## 1. Personvernerklæring (Privacy Policy)

Apple krever en lenke til en personvernerklæring, både i App Store Connect
(metadata-feltet "Privacy Policy URL") og lett tilgjengelig inne i appen
(App Store Review Guidelines 5.1.1(i)). Appen har allerede en "Personvern"-
seksjon i Om-fanen, men den er ikke en formell erklæring med URL — det
holder ikke alene.

**Løsning:** Publiser teksten under et sted du kontrollerer en URL til,
f.eks. en enkel side på GitHub Pages, Notion (offentlig delt side), eller
rusinnsikt.no når den er satt opp. Si ifra så hjelper jeg deg publisere den
som en enkel nettside.

---

**Personvernerklæring for Rusinnsikt**

*Sist oppdatert: [dato]*

Rusinnsikt er laget for å kreve så lite av deg som mulig — også når det
gjelder personvern.

**Vi samler ikke inn noe.** Appen har ingen brukerkonto, ingen innlogging,
og ingen analyse- eller sporingsverktøy. Det sendes ingen data til oss
eller til tredjeparter mens du bruker appen.

**Ingen internettforbindelse kreves.** Alt faktainnhold i appen er lagret
lokalt på enheten din. Appen tar ikke kontakt med noen server for å vise
deg informasjon om rusmidler, nødnumre eller førstehjelp.

**Det eneste som lagres lokalt** er én innstilling — om du har sett
velkomstskjermen (onboarding) — lagret via iOS' innebygde UserDefaults,
kun tilgjengelig for appen selv. Dette lagres aldri utenfor enheten din.

**Frivillig støtte (tip jar):** Hvis du velger å støtte appen økonomisk,
håndteres kjøpet av Apple via App Store — vi mottar aldri betalingsinfo,
kortnummer eller annen finansiell informasjon.

**Lenker ut av appen:** Om-fanen inneholder lenker til rusinfo.no,
rusopplysningen.no og App Store. Disse eksterne sidene har sine egne
personvernvilkår.

**Kontakt:** Spørsmål om personvern kan sendes til
elofsson.martin@gmail.com.

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

## 4. Kort beskrivelse / nøkkelord til App Store-oppføringen (utkast)

**Undertittel (30 tegn maks):** Fakta om rusmidler og hjelp

**Nøkkelord (kommaseparert, 100 tegn maks):**
rus,narkotika,rusmidler,skadereduksjon,overdose,nødhjelp,giftinformasjon,fakta,helse,ungdom

**Beskrivelse (utkast — kan forkortes/tilpasses):**

Rusinnsikt er et nøytralt, faktabasert oppslagsverk om rusmidler og risiko
— laget for å gi ungdom og andre lettforståelig informasjon, ikke for å
oppfordre til bruk.

• Fakta om virkning, risiko og skadereduksjon for et bredt utvalg
  rusmidler, hentet og forkortet fra rusinfo.no og rusopplysningen.no
• Rask tilgang til nødnumre og overdoseveiledning
• Ingen konto, ingen internettforbindelse kreves, ingen sporing
• Helt gratis — frivillig, valgfri støtte til utvikleren

Appen er et uavhengig informasjonsprosjekt og er ikke offisielt tilknyttet
rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.

Ved mistanke om overdose eller forgiftning: ring alltid 113 eller
Giftinformasjonen.

## 5. Support-URL

App Store Connect krever en support-URL (ikke bare e-post). Enkleste
løsning: en enkel side på rusinnsikt.no, eller en gratis side (GitHub
Pages/Notion) med kontakt-e-post og en kort FAQ. Si ifra så bygger jeg en.
