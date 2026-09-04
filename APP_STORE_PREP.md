# Rusinnsikt — forberedelser for App Store-lansering

Utkast forberedt av Claude 2026-09-04. Alt innhold nedenfor er utkast — les
igjennom og juster før du bruker det.

## 1. Personvernerklæring (Privacy Policy) — PUBLISERT

Publisert som en egen side (personvern + support i ett):
https://claude.ai/code/artifact/84a0a41b-b1a3-4217-9769-aefaa79f5109

**VIKTIG — gjør dette før du limer inn URL-en i App Store Connect:** Claude-
artefakter er private til du deler dem. Åpne siden, trykk delingsmenyn og
gjør den offentlig/delbar — ellers kan ikke Apples granskere åpne lenken.

Lenken er også lagt inn i appens "Personvern"-seksjon i Om-fanen (commit
kommer), og kan brukes i BÅDE "Privacy Policy URL" og "Support URL"-feltene
i App Store Connect siden siden dekker begge deler.

Vil du heller ha den på en egen domenetilknyttet URL (f.eks. rusinnsikt.no/personvern)
senere, kan samme HTML-innhold flyttes dit når domenet er satt opp — si ifra.

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

## 5. Support-URL — LØST (samme side som punkt 1)

Support-seksjonen og en FAQ er inkludert på samme publiserte side som
personvernerklæringen (se punkt 1). Samme URL kan brukes i "Support URL"-
feltet i App Store Connect.
