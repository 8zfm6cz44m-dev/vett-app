# Överlämning – Rusinnsikt – Planering
Skapad: 2026-10-05 från chatten "App - Rusinnsikt" (Cowork, projekt App - Vett)
Se även: Claude_Overlamning_Kodning_20261005.md

## Vad Rusinnsikt är
Norskspråkig offline-app (iOS + Android) med neutral, faktabaserad information om 29 rusmidler, nødhjelp/overdoseveiledning och sök. Syfte: förebyggande uppslagsverk, uppmuntrar inte till bruk. Utgivare/utvecklare heter **Devlyn IT** överallt (Martins ENK-varumärke); kontakt **info@rusinnsikt.no**. Martins eget namn får inte förekomma någonstans i appen, dokumentationen som publiceras eller det publika repot.

## Nuläge (2026-10-05)
- **Build 7 (1.0) ligger i TestFlight** sedan 25 sept med release notes. Arbetskatalogen har en ocommittad bump till build 8 i pbxproj (osäkert om build 8 är uppladdad – fråga Martin).
- **App Store Connect är ifyllt**: Support URL devlyn.no/rusinnsikt/, Privacy Policy URL devlyn.no/rusinnsikt/personvern/ (publicerad "Data Not Collected"), copyright "2026 Devlyn IT", reviewer-notat, 6 skärmbilder i rätt ordning, TestFlight-info med info@. **Inte inlämnat för granskning** – Martin vill stanna på TestFlight tills vidare. Build 4 är fortfarande kopplad till versionssidan; måste bytas till senaste build före inlämning.
- **Support/personvernsidor live** på devlyn.no (ersatte Google Sites). Google Sites-sidan kan avpubliceras.
- **Repon**: `vett-app` (privat, full historik, arbetsrepo, remote heter fortfarande vett-app) och `rusinnsikt` (publikt, en enda commit som Devlyn IT, ren historik, ligger lokalt i `~/Developer/rusinnsikt`). Publika repot speglas manuellt vid release.
- **Mappen är omdöpt** från Vett till Rusinnsikt (2026-10-05). Tomma kvarlämnade mappar `Vett/RusFaktaApp` i projektroten kan tas bort. Mappen ligger i `~/Documents` som troligen synkas med iCloud – det har gett git-problem (mmap-fel, låsfiler); rekommenderat att flytta till `~/Developer/` när det passar.
- **Innehållet** är faktakontrollerat två gånger (18 och 24 sept) mot norska myndigheter, RUSinfo (Oslo kommune), kliniska källor. Logg och källbas ligger i Claude-projektet "App - Vett": `claude/faktakoll-log.md`, `claude/kildereferanser-rusmidler.md`.

## Beslut (Martins) med motivering
- **Anonymitet är viktigast**: ingen spårning, inget konto, inga nätverksanrop (Android saknar INTERNET-permission; iOS har ingen nätverkskod), en enda lokal boolean, innehållet döljs i app-växlaren. Löftet får inte överdrivas: en MDM-styrd jobbtelefon kan lista installerade appar – det är telefonen, inte appen. Se PERSONVERN_AUDIT.md.
- **Innehållskällor**: bara norska myndigheter, internationella reglerande organ och fagfellevurderad litteratur ("10000 % legit"). rusopplysningen.no (Foreningen Tryggere Ruspolitikk) endast som andrakälla. RUSinfo (Velferdsetaten, Oslo kommune) är fullvärdig primärkälla.
- **Risikoreduksjon-innehåll** ("Hvis noen likevel skal bruke") finns, efter feedback från en testare. Gräns: aldrig doser, mängder, tablettantal eller doseringsintervall – skyddat av tester på båda plattformarna.
- **Namn**: appen heter Rusinnsikt; Xcode-target, modul, mappar och testmål bytte namn från Vett 25 sept. Bundle-id `no.rusinnsikt.app` oförändrat.
- **Devlyn IT** som utvecklare (inte "Devlyn"), fastslaget 25 sept. Om-skärmen säger "Rusinnsikt er utviklet av Devlyn IT".
- **Öppen källkod** valdes för att vem som helst ska kunna verifiera att appen inte spårar. Därför ett nytt rent repo i stället för att göra vett-app publikt (historiken innehåller namn/Gmail).
- **Apple-konto**: Individual-konto → App Store visar Martins namn som Seller. Konvertering till Organization är gratis via Apple Support men kräver juridisk person (AS), inte ENK. Väntar.

## Avfärdade vägar
- Claude pushar till GitHub från Cowork – går inte (proxy tillåter inte repot). Martin pushar själv; gh CLI är nu inloggat.
- Google Sites som support/personvernsida – ersatt av devlyn.no.
- Göra vett-app publikt – nej, historiken läcker namn/Gmail och GitHub cachar.
- rusinnsikt.no som sidadress – fortfarande ett alternativ om Martin vill distansera appen från devlyn.no (där grundaren namnges), men devlyn.no valdes.

## Öppna frågor
- Är build 8 uppladdad till TestFlight? (pbxproj säger 8, ocommittat.)
- Nya skärmbilder från aktuell build (nuvarande är från 12 sept, före "utviklet av Devlyn IT").
- **Forenklet forelegg**-forskriften (kokain/amfetamin/metamfetamin) hade høringsfrist 1 okt 2026 – kontrollera om den trätt i kraft; påverkar legalStatus-texter.
- HHC/karisoprodol i forskrift 2025-12-19-2928 kunde inte verifieras (lovdata blockerar) – manuell kontroll.
- DMP-høring om zopiklon; eventuellt förbud mot nikotinfria e-sigaretter.
- Android-versionen finns i repot men har aldrig publicerats (inget Google Play-konto nämnt).
- Flytta projektmappen ut ur iCloud (`~/Developer/`).

## Prioriterad roadmap
1. Bekräfta build 8-status; committa pbxproj.
2. Juridisk snabbkoll efter 1 okt (skillen "faktakoll-rusinnsikt, bara juridisk status").
3. Nya skärmbilder; byt build på versionssidan i App Store Connect.
4. När Martin är redo: "Add for Review". Guide: projektdoc `claude/app-store-connect-guide.md` + APP_STORE_PREP.md.
5. Spegla till publika repot `rusinnsikt` vid varje release (export utan Utvecklingsanteckningar, APP_STORE_PREP.md, Team-ID, namn).
6. Android-release (Google Play) – inte påbörjat.
7. Organization-konto hos Apple när AS finns.

## Arbetssätt med Martin
Svenska i chatten, norsk bokmål i appen. Fråga inte om självklarheter. Claude ändrar aldrig i App Store Connect utan uttrycklig begäran (gjordes en gång 25 sept på begäran, via webbläsaren). Inga betalda tjänster utan ok. iOS och Android ska alltid ha byte-identiska Substances.json.
