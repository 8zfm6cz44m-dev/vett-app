---
name: faktakoll-log
description: Löpande logg över veckovisa automatiska faktakoll-körningar av Substances.json (App - Vett). Läs för historik över vad som granskats och ändrats per körning.
sources: [cowork]
---

## 2026-08-21

Veckovis faktakoll kört. En fullständig granskning av alla 28 ämnen hade redan gjorts tidigare samma dag (se claude/faktakoll-2026-08-21.md) med två fixar committade (xylazin-formulering i Opioider, e-sigaretter EU-direktiv-formulering). Denna körning gjorde riktade kontroller istället för full omkörning samma dag:

- **Forenklet forelegg-reformen** (avkriminalisering kokain/amfetamin/metamfetamin): bekräftat via regjeringen.no/agderposten att høringen sändes ut 26 juni 2026, høringsfrist 1 oktober 2026, reformen fortfarande INTE i kraft. legalStatus-texterna stämmer fortsatt ("ulovlig narkotisk stoff").
- **Nikotinfria e-sigaretter**: bekräftat via helsedirektoratet.no att de fortfarande är lagliga att sälja (endast tobakssmak/smaklös), förbudsförslaget fortfarande olöst. Ingen ändring behövs.
- Sökningar efter "lovendring 2026" hos dmp.no/helsedirektoratet.no/toll.no gav inga nya, daterade och konkreta ändringar som träffar något av de 28 ämnena.

**Resultat: inga ändringar i Substances.json denna vecka.** Inga nya commits.

## 2026-08-27

Veckovis faktakoll kört (6 dagar efter förra körningen). Klonade repot (senaste commit `9fe0261`, master oförändrad sedan 2026-08-21-fixarna). Riktade kontroller mot de två stående bevakningspunkterna plus generell nyhetssökning:

- **Forenklet forelegg-reformen**: dubbelkontrollerad från flera vinklar (Rett24, Stortinget dok8-202526-042s, NRK, Aftenposten, adressa.no m.fl.). Bekräftat att förslaget (30 juni 2026, høringsfrist 1 oktober 2026) fortfarande bara är på remiss — inget stortingsvedtak om ikraftträdande. En Rett24-artikel om en separat fråga (registrering av forenklede forelegg i strafferegistret) klargjorde dessutom att kokain/amfetamin/metamfetamin formellt förblir "ulovlige narkotiske stoffer" oavsett denna praxisdebatt. Ingen ändring i legalStatus.
- **Nikotinfria e-sigaretter**: bekräftat direkt via helsedirektoratet.no (uppdaterad 12 maj 2026) — fortsatt lagligt att importera/sälja, förbudsförslaget fortfarande olöst. Ingen ändring.
- Sökt brett efter "lovendring narkotika 2026", nya stoffer på DMP:s narkotikaliste, HHC/semisyntetiska cannabinoider (inte del av appens 28 ämnen — ingen åtgärd), samt allmänna nyheter om rusmidler/narkotika från senaste veckan. Inget nytt som träffar något av de 28 ämnena hittades.

**Resultat: inga ändringar i Substances.json denna vecka.** Inga nya commits.

## 2026-08-31

Veckovis faktakoll kört (4 dagar efter förra körningen). Klonade repot (senaste commit `9fe0261`, master oförändrad sedan 2026-08-21-fixarna). Riktade kontroller mot de stående bevakningspunkterna plus bredare sökningar:

- **Forenklet forelegg-reformen**: dubbelkontrollerad igen, inklusive två nya Rett24-artiklar om en pågående debatt kring registrering av forenklede forelegg i strafferegisteret (en teknisk/praktisk delfråga, ej relaterad till om avkriminaliseringen trätt i kraft). regjeringen.no bekräftar uttryckligen att høringsforskriften "følger opp lovendringer som ble vedtatt av Stortinget i juni 2025, og som ennå ikke har trådt i kraft", høringsfrist fortsatt 1 oktober 2026. legalStatus för kokain/amfetamin/metamfetamin ("ulovlig narkotisk stoff i Norge") stämmer fortsatt. Ingen ändring.
- **Nikotinfria e-sigaretter**: bekräftat via helsedirektoratet.no (sist uppdaterad 12 maj 2026) — fortsatt lagligt att importera/sälja (endast tobakssmak/smaklös), förbudsförslaget fortfarande olöst. Ingen ändring.
- **Xylazin**: bekräftat direkt via rusopplysningen.no att xylazin fortfarande inte är påvist i norska beslag eller biologiska prover. Nuvarande text i appen ("foreløpig ikke påvist") stämmer fortsatt. Ingen ändring.
- **DMP narkotikalisten**: hittade en pågående høring om att lista en generisk nitazen-grupp (teknisk utvidgning, nitazener är redan "Ulovlige narkotiske stoffer i Norge" enligt appens post för nya-opioider — ingen saklig ändring behövs) samt en høring om zopiklon (inte ett av appens 28 ämnen — ingen åtgärd). Ingen av dessa är beslutade ännu.
- Sökt brett efter "lovendring narkotika 2026", HHC/semisyntetiska cannabinoider (fortfarande inte del av appens 28 ämnen), samt allmänna nyheter från rusopplysningen.no, fhi.no och toll.no. Inget nytt som träffar något av de 28 ämnena hittades.

**Resultat: inga ändringar i Substances.json denna vecka.** Inga nya commits.

## 2026-09-12 — Djupdykning: blandingsrisiko (mixingRisks)

På Martins explicita begäran: en riktad, källkritisk djupdykning i `mixingRisks`-fältet för samtliga 28 ämnen (vad som händer vid blandning med andra substanser, hur farligt det är) — inte en rutinmässig veckokontroll av juridisk status som tidigare körningar. Bakgrund: Martin flaggade två specifika, kända livsfarliga kombinationer som måste vara korrekt och tydligt täckta: (1) bensodiazepiner + opioider + pregabalin/Lyrica, (2) tramadol + alkohol.

Fem parallella forskningspass (samma ämnesgruppering som 2026-08-21) gick igenom alla 28 ämnen mot i första hand norska myndighets-/fackkällor (Felleskatalogen, rusopplysningen.no, RELIS, FHI, Legemiddelhåndboka, Helsebiblioteket, Store medisinske leksikon, KORUS) och internationella källor som komplement (FDA, EMA, CDC, NIDA, peer-reviewed studier) där norsk täckning saknades. Full källista och exakt gammal/ny text per ämne finns i **claude/faktakoll-blandingsrisiko-2026-09-12.md**.

**Resultat: 14 av 28 ämnen fick uppdaterad `mixingRisks`-text, applicerad identiskt i BÅDA `Vett/RusFaktaApp/Resources/Substances.json` (iOS) och `android/app/src/main/assets/substances.json` (Android) — filerna verifierat byte-identiska efter ändringen.**

Uppdaterade: kokain, crack, amfetamin, metamfetamin (kokaetylen + opioid/GHB-maskeringsrisk tillagd), benzodiazepiner, opioider, pregabalin (bensodiazepin+opioid+pregabalin-trippelkombinationen nu explicit namngiven i alla tre, med 68 %-siffra från Gomes et al. 2018 via Felleskatalogen/Lyrica samt FDA:s 2019-varning; tramadol+alkohol-risk tillagd i opioider-texten), ketamin, lystgass (andningsdepression vid kombination med opioider/GHB/bensodiazepiner tillagd, inte bara fallrisk), poppers (hiv-läkemedelsmekanismen korrigerad till proteashämmare/CYP3A4 istället för generisk "påverkar blodtrycket"), dmt (SSRI/SNRI/tramadol namngivna explicit), xylazin (**viktigaste enskilda fyndet**: nalokson upphäver INTE xylazinets egen sedering, saknades helt i tidigare text), nps (konkretiserat varför blandning är extra riskabelt för kategorin), kratom (nyanserad — mitragynin ger inte mätbar andningsdepression ensamt, men bensodiazepin/alkohol-kombinationen är väldokumenterat en av de farligaste vid kratom-dödsfall).

Oförändrade (verifierade som redan korrekta mot minst en officiell/stark källa): alkohol, cannabis, mdma, ghb, nye-opioider, lsd, fleinsopp, khat, snus, e-sigaretter, 2cb, mefedron, syntetiske-cannabinoider, zolpidem.

**Kvarstående osäkerheter:** några av rusopplysningen.no:s enskilda ämnessidor (kombinasjon-sektioner) är JS-renderade och kunde inte hämtas fullständigt automatiserat denna session (ketamin, lystgass, poppers, kratom särskilt); rusinfo.no (OUS) blockerade all automatiserad hämtning. Innehållet motsäger inte dessa sidor, men en manuell kontroll rekommenderas som sista verifiering. JSON-synken mellan iOS/Android är fortfarande manuell — Martin har bekräftat (2026-09-12) att en automatisk lösning byggs senare.

## 2026-09-12 — Ny substans tillagd + juridisk statuskontroll (kokain/amfetamin/metamfetamin, ketamin, e-sigaretter, nitazener)

Uppföljning på Martins fråga "Inga nya substanser att lägga till och inga uppdateringar på dom jag har?" — en genuin verifiering, inte bara ett antagande.

**Ny substans tillagd: "Semisyntetiske cannabinoider (HHC, THCP m.fl.)" (id `semisyntetiske-cannabinoider`), appen har nu 29 ämnen.** Motivering: rusopplysningen.no — appens egen huvudkälla — har en egen dedikerad sida för denna ämnesgrupp, tydligt skild från appens befintliga "Syntetiske cannabinoider (Spice)"-post (annan tillverkningsmetod, annan och mer oklar juridisk status, säljs öppet i norska butiker/nätshoppar som vapes/gummier till ungdom). Innehåll byggt på rusopplysningen.no, Store medisinske leksikon, Ung.no (Bufdir) och en fagfellevurdert sjukhuskasuistik i **Ugeskrift for Læger** (allvarlig förgiftning, 2,5 dygns sjukhusvistelse efter HHC-P) samt Helsebibliotekets behandlingsanbefaling för syntetiske cannabinoider som analog akutthantering. Juridisk status hålls medvetet nyanserad: HHC är FN-reglerat och regnas som receptbelagt legemiddel i Norge (ulovlig utanför resept), väntas läggas till narkotikalistan men är det formellt ännu inte; Delta-8/THCP ligger i en uttalad gråzon men efterforskas ändå ofta som narkotikabrott. Tillagd identiskt i båda `Substances.json`-filerna (iOS + Android), verifierat byte-identiska efteråt.

**Forenklet forelegg-reformen (kokain/amfetamin/metamfetamin):** omkontrollerad igen. regjeringen.nos egen sida bekräftar uttryckligen att juni 2025-lovendringen "ennå ikke er trådt i kraft" och att høringsfristen för den implementerande forskriften fortfarande är 1 oktober 2026 — dvs samma status som i alla tre tidigare loggposter. En Rett24-artikel ("Full forvirring om rusreform og forenklede forelegg") bekräftade att den pågående debatten gäller en principfråga om straffregistrering, inte ikraftträdandedatumet. **Ingen ändring i legalStatus** — texterna ("ulovlig narkotisk stoff i Norge") stämmer oavsett reformens status, eftersom en förenklad bot fortfarande är en straffreaktion, inte en avkriminalisering.

**Ketamin:** kontrollerad mot DMP:s nyhet om att ketamin/esketamin fördes upp på narkotikalistan **1 januari 2026** — detta datum har nu passerat. Appens befintliga legalStatus-text för ketamin täcker redan exakt detta korrekt (skriven i en tidigare session) — ingen ändring behövdes.

**Nikotinfria e-sigaretter:** omkontrollerad mot helsedirektoratet.no och den nya tobaksforskriften (2026-03-27-526). Nikotinfria e-sigaretter (endast tobakssmak/smaklös) är fortfarande lagliga att sälja/importera, förbudsförslaget fortfarande olöst. Appens text stämmer redan — ingen ändring.

**Nitazen-generisk grupp (DMP):** en separat, nyare høring ("forslag til oppføring av ny generisk gruppe på narkotikalisten") hittades utöver 2023-listningen — men appens legalStatus för nya-opioider ("Ulovlige narkotiske stoffer i Norge") är redan generisk nog att stämma oavsett høringens utfall. Ingen ändring.

**Resultat: en ny substans tillagd (identiskt i båda plattformarnas JSON-filer), inga ändringar i befintliga 28 ämnens legalStatus.** `claude/kildereferanser-rusmidler.md` uppdaterad med två nya källor som tillkom under denna körning (Ung.no, Ugeskrift for Læger).

## 2026-09-18 — Nytt innehåll: risikoreduksjon («Hvis noen likevel skal bruke»)

Bakgrund: en användare i TestFlight-gruppen föreslog att appen även ska ge råd om vad man bör tänka på *om* man ändå tar något — t.ex. att festsällskap håller koll på varandra och skriver upp tider vid GHB. Martin bad om en bedömning mot Apples riktlinjer och beslutade sedan "kör på". Bedömning: Apple 1.4.3 förbjuder appar som *uppmuntrar* bruk och 1.4.2 begränsar *dosinformation*; skadereduktion från hälsomyndigheter är varken det ena eller det andra. Gränsen sattes därför vid **inga doser, mängder eller doseringsintervall som instruktion** — bara vad som ökar och minskar risk, i tredje person / imperativ om vad man undviker.

**Innehåll tillagt (commit `38673ea`, build 4), identiskt i båda Substances.json:**
- Nytt toppnivåfält `generalRiskReduction` (titel, intro, 8 regler, källnot) — visas som kort "Hvis noen likevel skal bruke" i Nødhjelp-fliken: ikke alene, ikke bland, ukjent innhold, tapt toleranse, vent før mer, hold oversikt (hvem tok hva/når), nalokson, ring 113.
- Nytt valfritt fält `riskReduction: [String]` på 11 ämnen — visas som kort "Slik reduserer du risikoen" på stoffsidan och ingår i deltexten: **ghb, mdma, kokain, crack, amfetamin, metamfetamin, ketamin, opioider, nye-opioider, benzodiazepiner, alkohol.** `sourceNote` utökad per ämne med källan för råden.

**Källor (verifierade genom hämtning 2026-09-18):**
- **KORUS "Fakta om GHB / MDMA / kokain / ketamin"** — faktaark utgivna av KORUS, RUSinfo och Uteseksjonen i Oslo med stöd från Helsedirektoratet (korus.no/vare-fagomrader/fakta-om-…). Primärkälla för GHB, MDMA, kokain/crack, ketamin och de stimulantia-principer som återanvänts för amfetamin/metamfetamin. OBS: KORUS-arken innehåller faktiska doseringsgränser (ml) — de togs medvetet **inte** med.
- **Helsedirektoratet, Nasjonal overdosestrategi 2019–2022** (regjeringen.no, PDF) — "ikke injisere aleine", "ikke blande ulike stoffer", tryggere inntaksmåter (Switch), nalokson nesespray, sterilt brukerutstyr, tolerans. Primärkälla för opioider, nye-opioider, benzodiazepiner och de generella reglerna.
- **Giftinformasjonen / Helsenorge, alkoholforgiftning** — dricktakt, mat, "svært beruset person bør ikke være alene", sideleie/hold varm, aspirasjonsrisk, 113/116 117. Primärkälla för alkohol.
- **Helsenorge "Forgiftninger med rusmidler"** — styrken varierer "fra 0 prosent til nesten 100 prosent" (grund för regeln om ukjent innhold).
- KORUS "fakta-om-heroin" gav 404; rusinfo.no blockerar fortsatt automatiserad hämtning (503) — opioidråden vilar därför helt på Helsedirektoratet + befintlig verifierad apptext (nalokson reseptfritt/blå resept).

**Skydd mot dosinnehåll i koden:** nya tester på båda plattformarna (`testRiskReductionNeverContainsDoses` / `riskReductionNeverContainsDoses`) failar om någon råd-text matchar `\d+ (mg|ml|g|gram|µg|…)`. Körd mot innehållet: 62 texter, 0 träffar.

**Kvarstående:** cannabis, lystgass, LSD/fleinsopp m.fl. har inga stoffspecifika råd ännu (de generella reglerna täcker dem). Om Martin vill utöka: samma metod, KORUS-arken först. Personvernerklæringen påverkas inte (inget nytt lagras).

## 2026-09-18 — Full djupsökning: samtliga 29 ämnen mot kliniska/officiella källor

På Martins begäran ("kör en full deep search så som du gjort tidigare … Det måste vara kliniska och 10000% legit rapporter det kommer från"). Fem parallella forskningspass (grupp A–E, samma ämnesindelning som tidigare) gick igenom **alla fält** på alla 29 ämnen plus de generella sektionerna (emergencyNumbers, generalEmergencyGuidance, generalRiskReduction). Källkrav: enbart kategori 1–3 i `claude/kildereferanser-rusmidler.md` — norska myndigheter, internationella reglerande organ, fagfellevurdert klinisk litteratur. rusopplysningen.no tilläts endast som andrakälla. Förslagen triagerades manuellt; de som inte kunde styrkas av en primärkälla applicerades **inte**.

**Resultat: 44 fält på 23 ämnen + 5 generella fält uppdaterade (46 ändringar, commit `9f6e8ea`).** Identiskt i båda `Substances.json` (iOS + Android), verifierat byte-identiska med `cmp`. Dos-regex mot alla riskReduction-texter: 0 träff. `scripts/personvern-sjekk.sh`: alla PASS.

**Viktigaste verifierade fynd (med källa):**
- **HHC / semisyntetiske cannabinoider**: HHC ställdes under FN-kontroll i december 2025 (CND/UNODC); Kripos bekräftar beslag i Norge. DMP:s sida om norsk listning är JS-renderad och kunde inte verifieras — därför **defensiv formulering** i legalStatus (FHI-baserad, inget påstått listningsdatum). Aliases utökade. *(Rättelse 2026-09-24: CND-beslutet togs i mars 2025; 6 december 2025 är ikraftträdandet.)*
- **Snus**: nikotinsnus (tobaksfritt) är **inte tillåtet** i Norge — Helsedirektoratet (uppdaterad 12 maj 2026). **Netthandelsforbud** för tobak och e-sigaretter från 1 jan 2026. Uppdaterat i snus och e-sigaretter.
- **Nye opioider (nitazener)**: den generiska nitazen-gruppen har stått på narkotikalisten **sedan maj 2023** (tidigare loggposter beskrev felaktigt en pågående høring — det var en senare utvidgning). Helsedirektoratet rapporterar **37 dödsfall** kopplade till nitazener. shortDescription, legalStatus, longTermRisks, sourceNote uppdaterade.
- **Benzodiazepiner**: bromazolam uppfört på narkotikalisten **15 okt 2024** (DMP). Mekanismen benzo+alkohol korrigerad från "farmakokinetisk" till **farmakodynamisk** (additiv GABA-effekt). emergencyAction/riskReduction justerade.
- **Opioider / nalokson**: Ventizolve nässpray **reseptfritt sedan 1 juli 2025** (DMP), fortsatt gratis via nalokson-programmet (KORUS). **Handlingsplan mot overdoser (Helsedirektoratet, aug 2025)** ersätter den utgångna Nasjonal overdosestrategi 2019–2022 — alla hänvisningar uppdaterade. **overdosefare.no** (varslingstjänst, lanserad 14 jan 2026) omnämnd i sourceNote.
- **Xylazin**: fortsatt **inte påvist** i Norge (OUS obduksjonsstatistikk / Kripos). sourceNote uppdaterad med datum.
- **Kokain/amfetamin/metamfetamin**: forenklet forelegg fortfarande **inte i kraft** (høringsfrist 1 okt 2026) — ingen ändring i legalStatus.
- **Ketamin**: ketamin/esketamin på narkotikalisten **1 jan 2026** (DMP) — legalStatus förtydligad; riskReduction justerad (blåsproblem/K-cramps, inte alene).
- **Mefedron**: FN-listad **2015** (inte 2009 som tidigare stod) — shortDescription + legalStatus.
- **Poppers**: påståendet om glaukom **borttaget** (ingen primärkälla stöder det); longTermRisks vilar nu på methemoglobinemi + makulaskada (fagfellevurdert).
- **MDMA**: PMMA-förgiftningar med **>30 norska dödsfall 2010–2014** (FHI/Kripos) tillagt i riskReduction som motivering till "ukjent innhold". *(Rättelse 2026-09-24: årtalen saknade primärkälla — se nedan.)*
- **GHB**: longTermRisks korrigerad (abstinens kan bli livshotande, delirium — Helsebiblioteket).
- **LSD / fleinsopp**: shortTermRisks + mixingRisks mot Helsebibliotekets behandlingsanbefalinger och Giftinformasjonen (förväxling med giftig sopp, serotonergt syndrom vid SSRI/litium).
- **Cannabis**: legalStatus (medisinsk cannabis via DMP/godkjenningsfritak), short/longTermRisks (psykoserisiko, CHS) och emergencyAction mot FHI/Helsenorge.
- **Alkohol**: legalStatus (aldersgrenser 18/20, Vinmonopolet) och mixingRisks.
- **Pregabalin**: longTermRisks + sourceNote (Felleskatalogen/Lyrica, FDA 2019).
- **Kratom / 2C-B / DMT / e-sigaretter**: legalStatus verifierad och preciserad (DMT: narkotikalistet, ayahuasca omfattas — SML). *(Rättelse 2026-09-24: kratom ÄR narkotikalistet sedan des. 2023 enligt Kripos — apptexten var rätt, loggraden här var fel.)*
- **Lystgass**: overdoseSigns preciserade (hypoksi, B12-relaterad nevropati vid gjentatt bruk).
- **Generella sektioner**: pårørende-linjen är **Ivareta** — etikett nu "(Ivareta) – hverdager 09–15, tirsdag også 18–21" (ivareta.no). `ifConsciousButHeavilySedated`: rådet om "mat eller drikke" **borttaget** (aspirasjonsrisiko — Helsenorge førstehjelp). `ifUnconscious`: sideleie + 113 förtydligat. generalRiskReduction regel 7 (nalokson reseptfritt/gratis) + sourceNote (Handlingsplan mot overdoser 2025).

**Verifierade som redan korrekta (inga ändringar):** crack, amfetamin, metamfetamin, khat, nps, zolpidem, kokain (utom riskReduction), samt övriga fält på de ämnen som bara fick enstaka justeringar.

**Källor som INTE gick att hämta automatiserat denna körning:** rusinfo.no (503 på de flesta sidor, enstaka lyckades), DMP:s nyhetssida om HHC (JS-renderad), KORUS fakta-om-heroin (404 — ersatt av fakta-om-heroin-og-opioider). Inget innehåll i appen vilar enbart på en källa som inte kunde läsas.

**Kvarstående / medvetet ej ändrat (svag källa):** lystgass mixingRisks har en mening om kombination som inte kunde styrkas fullt ut (behållen, oförändrad); ketamin mixingRisks (lystgass/cannabis) — förslag förkastat pga svag källa; dmt overdoseSigns "kraftig forhøyet puls" kan mjukas upp vid nästa körning.

**Tidigare loggfel rättade genom denna körning:** 2026-08-31 och 2026-09-12 beskrev nitazen-gruppen som en *pågående* høring — gruppen har varit listad sedan maj 2023.

## 2026-09-24 — Djupsökning nr 2: alla 29 ämnen + RUSinfo som fullständig primärkälla

På Martins begäran ("Dags att göra ett djupdyk … Var säker på att denna också är med: https://rusinfo.no/fakta-om-rus-og-legemidler/"). Samma metod som 2026-09-18 (fem parallella pass, grupp A–E, alla fält, kategori 1–3-källor, rusopplysningen.no endast som andrakälla), men med en viktig skillnad: **RUSinfo (rusinfo.no) blockerar fortfarande all automatiserad hämtning (robots.txt svarar 503, proxy 403 från Macen), så hela sajtens "Rusmidler A-Å" hämtades i stället via Claude Browser på Martins Mac** — 29 sidor inklusive alla undersidor (virkninger, blanding, overdose, loven m.m.; 388 000 tecken) sparades som text och lästes av varje pass. RUSinfo bekräftat vara **Velferdsetaten i Oslo kommune, med tilskudd från Helsedirektoratet** (rusinfo.no/om-rusinfo) → kategori 1. RUSinfo saknar sidor om snus, e-sigaretter, HHC, xylazin och kratom.

**Resultat: 48 fält uppdaterade på 24 ämnen + 2 generella fält (commit `559d4d3`, 2026-09-25 — Martin hade lagt den levererade filen i en nedladdningsmapp i repot; den flyttades till båda rätta sökvägarna, personvern-sjekk alla PASS).** Identiskt i båda `Substances.json`. Dos-regex mot alla riskReduction-texter: 0 träff. Inga URL:er i innehållet.

**Sakfel rättade (viktigast först):**
- **Zolpidem — legalStatus var fel.** Appen sa "ikke narkotikaklassifisert". Zolpidem står i narkotikaforskriftens lista (rad «Zolpidem P IV», verifierat mot lovdata) och har varit i FN:s psykotropkonvention liste IV sedan 2001 (UNODC). Det är **zopiklon** som inte är listad (DMP-høring pågår). Reseptgruppe B bekräftad (Felleskatalogen/Stilnoct).
- **Crack — shortDescription innehöll en tillverkningsbeskrivning** (bakepulver/ammoniakk). Borttagen; ersatt med NIDA/FHI-baserad beskrivning.
- **Semisyntetiske cannabinoider (THCP) — "minst ett rapportert selvmord" var fel.** Kasuistiken (Greer et al. 2025, PMID 39906186) gäller ett självmords*försök* under rusutlöst psykos. Rättat. Det danska HHC-P-fallet (Ugeskrift for Læger 2024) rapporterade inga kramper — overdoseSigns rättat. **HHC: CND-beslutet togs i mars 2025 (68:e sessionen), ikraftträdande 6 december 2025** (UNODC) — förra loggposten/apptexten angav december som beslutsdatum. Norsk listning via forskrift 2025-12-19-2928 kunde INTE verifieras (lovdata robots, dmp.no JS) — FHI-baserad defensiv formulering behållen.
- **Opioider/tramadol — mixingRisks kopplade alkohol till serotonergt syndrom** (fel mekanism). Felleskatalogen (Nobligan): alkohol → CNS-depression + sänkt kramptröskel; serotonergt syndrom vid SSRI/SNRI/MAO-hemmare (RUSinfo lägger till MDMA). Rättat.
- **Alkohol — metanol-latens "12–24 timer"** → Giftinformasjonen (30.06.2026): 10–30 timmar, senare vid samtidigt etanolintag.
- **Nye opioider — emergencyAction saknade HLR/pustehjelp och sideleie** (Helsenorge/Helsedirektoratet). Tillagt. longTermRisks: OUS obduksjonsstatistikk 13 (2023), 18 (2024), **under 5 (2025, publ. april 2026)** + Helsedirektoratets 37 totalt.
- **Xylazin — emergencyAction påstod "høyere doser nalokson"** (gäller nitazener, inte xylazin; ingen källa). Ersatt med CDC: ge nalokson ändå, innblåsninger särskilt viktiga, ingen etablerad antidot (Helsebiblioteket 2026).
- **Kokain — riskReduction "Kripos har påvist ketamin i kokain"** var oprecist: Kripos (1. halvår 2024) analyserade «pink cocaine» = ketamin+MDMA utan kokain. Renhet 30–95 % (snitt 73 %, Kripos 2025). Rättat.
- **MDMA — PMMA-siffran ">30 dødsfall 2010–2014"** (införd 2026-09-18) hade ingen primärkälla med de årtalen. Ersatt med dokumenterat: 27 dödsfall juli 2010–aug 2012 (FHI via Tidsskriftet 2013), "flere titalls" efter 2010.
- **Cannabis** — THC-halt uppdaterad (Kripos 2025: 25–29 % sedan 2018, 34 % 2025), CHS tillagt i longTermRisks, "tilby mat eller søt drikke" borttaget ur emergencyAction (ingen källa; RUSinfo/Helsebiblioteket: ro, trygg person), mixingRisks-mening som blandade ihop två RUSinfo-avsnitt rättad.
- **Metamfetamin** — "kan virke et helt døgn" → 8–14 timer (KORUS); "varig nevrotoksisk skade" nyanserad (NIDA: delvis reversibelt).
- **Kratom** — mixingRisks överdrev benzo/alkohol som "blant de dødeligste": CDC MMWR 2019 visar fentanyl 65 %, heroin 33 %, benzo 22 %, alkohol 12,5 %. Omskrivet med opioider först.
- **Lystgass** — mixingRisks-meningen om sentralstimulerende/hjertet (flaggad 2026-09-18) saknade källa; ersatt med KORUS cannabis-varning (blodtrykksfall).
- **Ketamin** — aliases var tomma (nu K, Special K, Kit Kat, kattevalium, supersyre — KORUS); lystgass/cannabis-kombinationer nu källbelagda (KORUS) och tillagda; tusi/"rosa kokain" tillagt i riskReduction (EUDA 2025).
- **2C-B** — tusi-varning tillagd i shortTermRisks (EUDA 2024/2026, Tolletaten 2025); "i Norge siden 2001" mjukat (bara FN-datumet är belagt).
- **Syntetiske cannabinoider** — akut njursvikt flyttad från långtids- till akutrisk (Helsebiblioteket); toleranse/abstinens tillagt (RUSinfo).
- **Snus** — «hvit snus» borttaget som synonym (missvisande: lovlig tobaksbaserad vit portionssnus); förtydligat att nikotinposer inte får tas med vid inresa heller (Helsedirektoratet 07.04.2026 + 31.08.2026).
- **E-sigaretter** — EU:s tobaksdirektiv i kraft i Norge **30 mars 2026** med nikotin-e-sig-förbudet vidarefört (regjeringen.no); "siden 2024" för smaksförbudet borttaget (ej verifierbart).
- **Benzodiazepiner** — legalStatus förtydligad (alla benzo narkotikalistade, inte bara designer-benzo); sourceNote pekade på utgången overdosestrategi → Handlingsplan 2025; "som regel forfalsket" om illegal Rohypnol mjukat (avregistrert 2004, Flunipam på godkjenningsfritak — RUSinfo).
- **DMT** — ayahuasca-varighet 4–8 t (RUSinfo), inte 4–6.
- **Generella sektioner** — `ifAgitatedOrHallucinating`: "Tilby mat eller drikke, gjerne noe søtt" borttaget (samma skäl som 2026-09-18); `generalRiskReduction` regel 2: "den vanligste årsaken til dødelige overdoser" (ingen källa) → kvalitativt (Helsedirektoratet: ca 70 % opioider, som regel flera stoffer).
- **sourceNote** på crack, metamfetamin, khat, poppers, mefedron, kratom, ketamin, lystgass, xylazin, 2cb, nye-opioider, zolpidem: icke-godkända källor borttagna (sundhed.dk, NHI.no) och de källor som faktiskt bär påståendena namngivna. Alkohol: saknat punktum.

**Verifierat oförändrat:** forenklet forelegg fortfarande INTE i kraft (høringsfrist 1 okt 2026 — bevaka!); ketamin narkotikalistet 1 jan 2026; lystgassforskriften från 1 juli 2026 (redan i appen); kratom narkotikalistet des. 2023 (Kripos bekräftar — obs: förra loggpostens rad om kratom var fel, apptexten var rätt); GHB alla fält; pregabalin alla fält; LSD/fleinsopp/khat/poppers/mefedron/NPS/amfetamin övriga fält; alla nödnummer och öppettider (113, 116 117, Giftinformasjonen 22 59 13 00 dygnet runt, RUSinfo 915 08 588 hverdager 11–18, Ivareta 800 40 567 hverdager 9–15 + tirsdag 18–21, Mental Helse 116 123, Kirkens SOS 22 40 00 40).

**Ohämtbart denna körning:** lovdata.no (robots — förutom huvuddokumentet i en 2016-version), dmp.no artikeltexter (JS), nalokson.uio.no (403), INCB green list (403), PMC (captcha), rusopplysningen.no kratom/poppers (JS). Inget nytt innehåll vilar enbart på en ohämtbar källa.

**Att bevaka:** (1) forenklet forelegg-forskriften efter 1 okt 2026; (2) HHC/karisoprodol i forskrift 2025-12-19-2928 — kontrollera manuellt på lovdata; (3) DMP-høring om zopiklon; (4) ev. utvidgat förbud mot nikotinfria e-sigaretter.

## 2026-10-05 — Djupsökning nr 3 (Claude Code): alla 29 ämnen + generella sektioner, RUSinfo hämtad på nytt

På Martins begäran, körd i Claude Code (inte Cowork). Metod: RUSinfo (rusinfo.no) hämtades via Claude Browser på Macen — 156 sidor (huvudsidor + undersidor ur "Rusmidler A–Å", ~780 000 tecken; `vallergan` gav 404). Texten lades i webbläsarens localStorage (nyckel `rusinfo_dump`) och lästes därifrån. Fem granskningsagenter (grupp A–E) enligt skillens brief; B, C och D stoppades första gången av säkerhetsspärren för läsning av localStorage via javascript_tool och kördes därför om respektive ersattes av manuell genomgång. D kördes om och fick läsa; B och C jämfördes manuellt mot RUSinfo av huvudsessionen.

**Resultat: 20 fält uppdaterade på 11 ämnen (6 grupp A, 13 grupp D, 2 egna). Identiskt i båda `Substances.json` (`cmp` OK). Dos-regex mot alla riskReduction + generalRiskReduction.rules: 0 träffar. Inga URL:er. `personvern-sjekk.sh`: alla PASS (binärkontrollen SKIP, ingen Release-build). iOS-tester (`xcodebuild test`, iPhone 17): TEST SUCCEEDED. Android-tester kunde inte köras (inget JDK på Macen) — dos-regexen kördes manuellt.**

**Rättat (viktigast först):**
- **Crack — varaktighet:** "5–10 min" / sniffad kokain "15–30 min" → "5–30 min" / "30–60 min" (FHI-notat 13.10.2025, tabell över inntaksmåter; RUSinfo kokain 15–60 min).
- **Cannabis — edibles:** "30–60 min, inntil 2–3 timer" saknade namngiven källa → RUSinfo: omkring en time, noen ganger lenger; vent flere timer før du tar mer.
- **Alkohol — overdoseSigns:** kramper och lav kroppstemperatur tillagda (RUSinfo alkoholbrosjyre).
- **Lystgass:** emergencyAction fick HLR om personen inte puster (samma rättelse som nye-opioider 2026-09-25); mixingRisks fick potensmidler (Viagra)/poppers → kraftigt blodtrykksfall; longTermRisks fick minne/konsentrasjon, psykiske plager och psykisk avhengighet (RUSinfo brosjyre). Cannabis/blodtrykk-meningen bekräftad.
- **Ketamin:** emergencyAction fick HLR + "gi aldri drikke til bevisstløs"; mixingRisks fick stimulanter → økt belastning på hjertet (RUSinfo/KORUS-brosjyren, tidigare förkastat som svagt belagt, nu direkt källa); longTermRisks utökad (hukommelse, særlig under 25 år; nedstemthet; rask toleranse); alias "jet" tillagt.
- **Poppers:** shortTermRisks fick økt puls och besvimelse; sourceNote: ung.no (ej godkänd källa) ersatt med RUSinfo, som själv citerar legemiddelforskriften § 1-3.
- **Mefedron:** effects/shortTermRisks utökade (svettetokter, aggressivitet, hjerterytmeforstyrrelser, nesesmerter, mild seksuell stimulering); longTermRisks: "mulige langvarige psykiske plager" saknade grund → "kunnskapen om langvarige skader er foreløpig begrenset" (RUSinfo).
- **Khat — legalStatus:** "dyrking" finns inte i RUSinfos uppräkning av förbjudna handlingar → omskriven efter narkotikaforskriften § 5 (tilvirkning, anskaffelse, omsetning, innførsel, utførsel, oppbevaring, besittelse, bruk). Lovdata ohämtbar — medium.
- **Amfetamin/metamfetamin — aliases:** "fort", "makka" resp. "meta" (RUSinfo).
- **LSD — emergencyAction:** "tilby mat (gjerne søtt)" → "tilby enkel mat og litt vann" ("gjerne søtt" saknade källa; RUSinfo: enkel mat og drikke).
- **DMT — overdoseSigns:** "Kraftig forhøyet puls" → "Tydelig forhøyet puls" (flaggan från 2026-09-24; RUSinfo: økt puls/blodtrykk).

**Rättelse av tidigare logg:** Bevakningspunkten "HHC/karisoprodol i forskrift 2025-12-19-2928" var fel formulerad. Enligt Lovdata är den forskriften endast en ändring som lägger till ketamin och esketamin (i kraft 1.1.2026). Var HHC/karisoprodol är listade i Norge är fortfarande INTE verifierat på primärkälla (en DMP-nyhetssammanfattning antyder listning utan nasjonal høring efter CND-beslutet; Finland: narkotika från 29.12.2025). Appens `semisyntetiske-cannabinoider.legalStatus` är defensivt formulerad och påstår inget fel; ändrades inte.

**Verifierat oförändrat:**
- **legalStatus:** Forenklet forelegg-forskriften är fortfarande inte vedtatt/i kraft (regjeringen.no "Under behandling"; høring 26.6–1.10.2026; lovendringen från 20.6.2025, lov nr 83, ej i kraft) → "ulovlig" för kokain/amfetamin/metamfetamin stämmer; gramgränserna i förslaget ska INTE in i appen. DMP-høring om zopiklon (20.3.2026, frist 15.5.2026): inget vedtak funnet; zolpidem-texten opåverkad. Ketamin narkotikalistet 1.1.2026 och lystgassreglerna från 1.7.2026 bekräftade. E-sigaretter: nikotin-e-sig-förbudet vidareförs (direktivet i kraft 30.3.2026); nikotinfrie fortfarande inte avgjort (toll.no, Helsedirektoratet 7.4.2026); snus/nikotinposer oförändrat.
- **Siffror:** Alkoholens åldersgränser (18/20 år); cannabis THC-halt (Kripos 2025); kokains renhet 30–95 %, snitt 73 % (Kripos 2025); nitazendödsfall 13 (2023) / 18 (2024) / <5 (2025) enligt OUS (RUSinfo anger 12 för 2023 — äldre siffra, appen behåller OUS).
- **Fält mot RUSinfo utan avvikelse:** MDMA, GHB, benzodiazepiner, zolpidem, pregabalin, opioider, nye-opioider, lsd, fleinsopp, dmt (övrigt), 2cb, ketamin, khat, poppers, snus, e-sigaretter, syntetiske cannabinoider, generalEmergencyGuidance, generalRiskReduction.
- **Nödnummer/öppettider:** 113, Giftinformasjonen 22 59 13 00 (dygnet runt), RUSinfo 915 08 588 (hverdager 11–18), Pårørendetelefonen 800 40 567 (hverdager 9–15, tirsdag også 18–21).

**Ej genomfört / flaggat:**
- Förslaget att RUSinfo-etiketten ska få "(stengt 14.30–15.00)" lades INTE in: kunde inte bekräftas i hämtad text (sidan om-rusinfo ingick inte i dumpen).
- Förslaget att ta bort meningen "Tomme beholdere er farlig avfall" (lystgass) drogs tillbaka: RUSinfo har ett eget avsnitt om det (2026).
- DMT mixingRisks ("ekstremt farlige… dødelig overstimulering… hjertestans") saknar direkt RUSinfo-belägg (RUSinfo: "potensielt svært alvorlig") men ändrades inte — varningen är medicinskt rimlig och ska inte försvagas utan bättre källa. Bör styrkas mot Helsebiblioteket vid nästa körning.
- Cannabis "betydelig økt risiko for psykose" bygger på forskning.no (svag källa) — bör nyanseras/styrkas manuellt.
- Opioider riskReduction "Røyking gir lavere overdoserisiko enn injisering": RUSinfo/Helsedirektoratet påpekar att även røyking och tabletter kan ge dødelig overdose — kan nyanseras.
- Pregabalin "68 % økt risiko": inte återverifierad mot Gomes et al. (PLoS Med 2017); bör kontrolleras mot PubMed.
- Ketamin mixingRisks-meningen om "stoffer som svekker balanse og koordinasjon eller senker blodtrykket" och mefedron mixingRisks (serotonergt syndrom) saknar RUSinfo-stöd; mefedron riskLevel "Lav (akutt dødelighet)" vs RUSinfo (rapporterade dödsfall) bör ses över.
- lystgass, poppers, khat och mefedron saknar riskReduction (RUSinfo har dosfria råd för lystgass och poppers) — innehållsbeslut, kräver spegling i båda apparna.

**Ohämtbart denna körning:** lovdata.no (robots; forskrift 2025-12-19-2928 hämtades av en agent men gäller bara ketamin/esketamin), dmp.no artikeltexter och høringsbrev-PDF (JS/ej läsbar), rusinfo.no/om-rusinfo (ej i dumpen), rusopplysningen.no (inte använd). Inget nytt innehåll vilar enbart på en ohämtbar källa, utom khat-ändringen (medium).

**Att bevaka:** (1) forenklet forelegg-forskriften (vedtak/ikraftsettelse efter høringsfristen 1.10.2026); (2) HHC/karisoprodol — kontrollera manuellt på lovdata/dmp.no i vilken forskrift och från vilket datum de är listade; (3) DMP-høring om zopiklon (vedtak?); (4) nikotinfrie e-sigaretter (avgörande/registreringskrav).
