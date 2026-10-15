---
name: "faktakoll-rusinnsikt"
description: "Kör en full källkritisk djupkontroll (faktakoll) av allt substansinnehåll i Rusinnsikt-appens Substances.json mot norska myndigheter, RUSinfo, Lovdatas öppna API och kliniska källor. Interaktivt på Macen: applicera och committa. Schemalagt/headless: endast förslag."
---

# Faktakoll Rusinnsikt (App - Vett)

Används när Martin ber om ett "djupdyk", "deep search", "faktakoll" eller "genomgång av substanserna", och av det schemalagda veckojobbet. Svara alltid på svenska; apptexter är norsk bokmål. Ändra aldrig kod, bara innehåll.

**Push-policy (2026-10-15): ingen Claude-session pushar till GitHub, inte heller det schemalagda jobbet. Martin pushar själv.** Repots `CLAUDE.md` (avsnitten "Push-policy" och "Verifieringskrav") är styrande; vid motsägelse gäller `CLAUDE.md`.

## Två lägen

- **Interaktivt på Macen** (Claude Code eller Cowork med ansluten Mac): rättelser som uppfyller verifieringskraven appliceras i båda `Substances.json`, testas och committas lokalt. Ingen push.
- **Schemalagt/headless** (klon från GitHub, ingen Mac): ingenting committas eller pushas. Resultatet är en rapport med förslag i formatet från steg 3 + en färdig loggpost, som Martin eller en Claude Code-session lägger in. Ange alltid vilken commit som granskats (`git rev-parse --short HEAD`) och jämför med föregående loggpost — om klonen är äldre än senaste loggpostens innehåll, säg det tydligt.

## 0. Läs först (i repot — källan till sanning)

Repo: lokalt `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Rusinnsikt`, på GitHub `8zfm6cz44m-dev/vett-app` (privat, branch `master`). Cowork-projektets `claude/*.md` är bara pekare och ska inte läsas eller uppdateras.

1. `CLAUDE.md` — integritetsregler, push-policy, verifieringskrav.
2. `Utvecklingsanteckningar/Claude_kildereferanser-rusmidler.md` — tillåtna källor (kategori 1–3), förbjudna källor, hämtningsbegränsningar.
3. `Utvecklingsanteckningar/Claude_faktakoll-log.md` — senaste loggposterna: vad som redan är verifierat, stående bevakningspunkter och "kvarstående".

## 1. Hämta aktuellt innehåll

- Interaktivt: i repot, `git log -3`, `git status`, `git log --oneline origin/master..HEAD` (opushade commits = GitHub är inaktuellt), `cmp Rusinnsikt/RusFaktaApp/Resources/Substances.json android/app/src/main/assets/substances.json` (måste vara identiska).
- Headless: klona `vett-app`, samma `cmp`. Gör inte research på en fil som uppenbart är äldre än senaste loggpost utan att flagga det.

## 2. Hämta källor

- **RUSinfo:** blockerar WebFetch (503) och curl (403). Fungerande metod på Macen: Claude Browser till `https://rusinfo.no/fakta-om-rus-og-legemidler/`, sedan `javascript_tool` med same-origin `fetch()` per undersida (huvudsida + virkninger/blanding/overdose/loven). Delegera extraktionen till EN subagent som skriver texten till filer så den inte hamnar i huvudkontexten. RUSinfo saknar snus, e-sigaretter, HHC, xylazin, kratom. Headless utan webbläsare: ange RUSinfo som ohämtbar.
- **Juridik: Lovdatas öppna API, aldrig lovdata.no** (robots blockerar). `https://api.lovdata.no` → `GET /v1/publicData/list` → `GET /v1/publicData/get/<filnamn>` (NLOD 2.0, ingen nyckel). Narkotikaforskriften = `sf/sf-20130214-0199.xml` i `gjeldende-sentrale-forskrifter.tar.bz2` (~21 MB); nya ändringsforskrifter i `lovtidend-avd1-<år>.tar.bz2` (~1,5 MB). Interaktivt: be Martin om ok med filnamn och storlek innan nedladdning. Headless: ladda ner bara de två nämnda filerna, inget annat.

## 3. Fem parallella subagenter (grupp A–E)

Dela `substances` i fem `group_X.json` (E får även emergencyNumbers, generalEmergencyGuidance, generalRiskReduction). Grupper: A alkohol/cannabis/kokain/crack/amfetamin/metamfetamin; B mdma/ghb/benzodiazepiner/zolpidem/pregabalin/opioider; C nye-opioider/xylazin/lsd/fleinsopp/dmt/2cb; D ketamin/lystgass/khat/poppers/mefedron/kratom; E snus/e-sigaretter/nps/syntetiske/semisyntetiske + generella.

Skriv en brief (`brief.md`) med: källreglerna (kategori 1–3, rusopplysningen.no = Foreningen Tryggere Ruspolitikk endast som andrakälla, EUDA inte EMCDDA, nyhetsmedier bara som spår), alla fält som ska granskas, krav på att `legalStatus` verifieras mot Lovdata-API eller myndighetssida daterad innevarande år, att `riskReduction` ALDRIG får innehålla doser/mängder/tablettantal, att siffror måste ha namngiven primärkälla med årtal, och svarsformatet: JSON-lista `{id, field, new (komplett ny text på bokmål), reason, sources[], confidence high|medium}` + `_meta {verified_ok, unfetchable, notes}`. Be varje agent skriva `result_X.json` OCH returnera JSON. Starta alla fem i samma meddelande.

## 4. Triage

Ett förslag räknas som **verifierat** bara om (a) en primärkälla (Lovdata, norsk myndighet, RUSinfo/Helsedirektoratet, FHI, DMP) otvetydigt motsäger nuvarande text, eller (b) minst två oberoende källor ur kategori 1–3 gör det. Annars: bevakningsnotis, ingen ändring. `legalStatus`, dödsfallssiffror och akut-/nödråd kräver alltid primärkälla och markeras "kräver Martins granskning". Verifiera själv (WebFetch/API) allt som är `medium`.

## 5. Applicering (endast interaktivt på Macen)

- Filen har egen formatering (inline-listor, små objekt på en rad) — `json.dumps` reproducerar den INTE. Formatbevarande ersättning: hitta `"field": <gammal literal>` inom substansens span och byt mot ny literal; index-fält (`rules[1]`) ersätts elementvis. Alltid i båda filerna, följt av `cmp`. Verifiera att `json.loads` ger väntat objekt.
- Dos-regex `\d+([.,]\d+)?\s?(mg|ml|g|gram|µg|mikrogram|milligram|milliliter|dl|cl)\b` på alla riskReduction + generalRiskReduction.rules → 0. `grep -c http` → 0.
- `bash scripts/personvern-sjekk.sh` (alla PASS), `xcodebuild test -scheme Rusinnsikt` (destination iPhone 17).
- `git add` båda JSON + loggen, commit på norska med fotnot enligt sessionens instruktion. **Ingen push.** Ta bort `.git/*.lock` bara vid behov och med ok.

## 6. Dokumentera

- Ny loggpost i `Utvecklingsanteckningar/Claude_faktakoll-log.md`: granskad commit, metod, antal fält, varje sakfel med källa, verifierat oförändrat, ohämtbara källor, att bevaka. Rätta tidigare loggposter om de visat sig felaktiga. Headless: loggposten levereras i rapporten, inte committad.
- Uppdatera `Utvecklingsanteckningar/Claude_kildereferanser-rusmidler.md` med nya källor, namnbyten och hämtningsbegränsningar; sätt `last_verified`.
- Rapportera till Martin på svenska: viktigaste rättelserna/förslagen först, vad han ska göra (pusha, ev. ny TestFlight-build), stående bevakningspunkter.
