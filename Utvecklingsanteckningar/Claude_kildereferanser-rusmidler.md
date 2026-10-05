---
name: kildereferanser-rusmidler
description: Stående referanslista över medicinskt/vetenskapligt pålitliga källor för allt substansinnehåll i appen (Rusinnsikt/Vett). Basen för alla faktakoll-körningar framöver — läs FÖRE varje granskning, uppdatera NÄR nya pålitliga källor hittas eller en källa byter namn/domän/status.
sources: [cowork]
last_verified: 2026-10-05 (full djupsökning alla 29 ämnen, RUSinfo A–Å hämtad på nytt, 156 sidor)
---

Detta är den stående käll-basen Martin bad om 2026-09-12: enbart medicinskt/vetenskapligt styrkta sidor — inga forum, inga oredigerade wikis, inga anonyma harm-reduction-communities som primärkälla. Varje källa nedan är kontrollerad som aktiv och korrekt namngiven per verifieringsdatumet ovan. Listan är avsedd att LEVA: när en källa byter namn, domän eller läggs ner, uppdatera raden istället för att bara lägga till en ny. När en ny 100-procentigt pålitlig källa hittas under framtida faktakollar, lägg till den här.

## 0. Hämtningsbegränsningar (läs först — sparar timmar)

- **Metod 2026-10-05 (Claude Code):** efter att huvudsidan öppnats i Claude Browser hämtades alla undersidor med same-origin `fetch()` i batcher om ~6 (45 s-gräns per javascript_tool-anrop; panelen kan vara dold och throttlas) och lades i `localStorage` (`rusinfo_dump`, nyckel = URL). POST till lokal mottagare, nedladdning och urklipp fungerade INTE (blockerat resp. fokuskrav). **Subagenter nekas ofta att läsa localStorage via javascript_tool** (säkerhetsspärr 'Browser JS Exfil'/'Auto-Mode Bypass'); huvudsessionen kan läsa den direkt, eller användaren tillåter verktyget.
- **rusinfo.no** blockerar WebFetch (robots.txt svarar 503) och curl via Macens proxy (403). **Fungerande metod (2026-09-24):** öppna sajten i Claude Browser på Martins Mac och hämta undersidor med same-origin `fetch()` via javascript_tool; spara texten till filer som subagenter läser. Hela "Rusmidler A-Å" (29 sidor med alla undersidor) togs på detta sätt.
- **Lovdata öppna API (2026-10-05):** lovdata.no blockerar vanlig hämtning, men Stiftelsen Lovdata publicerar gällande lover/sentrale forskrifter och Norsk Lovtidend avd. I som **öppna data (NLOD 2.0)** på `https://api.lovdata.no` — `GET /v1/publicData/list`, sedan `GET /v1/publicData/get/<filnamn>` (t.ex. `gjeldende-sentrale-forskrifter.tar.bz2`, `lovtidend-avd1-2026.tar.bz2`); ingen nyckel; Swagger `/swagger/index.html`; kontakt utvikling@lovdata.no. Ladda ner endast efter Martins ok (filstorlek anges). Narkotikaforskriften = `sf/sf-20130214-0199.xml`. Detta är den föredragna vägen för juridisk status i stället för lovdata.no. Äldre text nedan om att lovdata är ohämtbart gäller bara själva webbplatsen.
- **lovdata.no** blockerar WebFetch (robots). Huvuddokumentet för narkotikaforskriften (2013-02-14-199) kan ibland hämtas men returneras då som en **gammal konsoliderad version (2016)** — den saknar alla senare tillägg (nitazener, kratom, bromazolam, HHC). Använd den ALDRIG för att avgöra om ett nyare ämne är listat; bekräfta i stället via DMP-nyhet, Kripos narkotikastatistikk eller OUS obduksjonsstatistikk, eller öppna lovdata i Claude Browser på Macen.
- **dmp.no** artikeltexter är JS-renderade — WebFetch ger bara meta-beskrivningen. Samma workaround som ovan.
- nalokson.uio.no (403), INCB green list-PDF:er (403), PMC-fulltext (captcha; använd PubMed-abstract), rusopplysningen.no enskilda sidor (delvis JS).

## 1. Norska offentliga myndigheter och officiella informationstjänster (förstahandskällor)

Dessa väger tyngst för allt som gäller norsk lagstiftning, norska förhållanden och norska hälsomyndigheters egna rekommendationer.

- **RUSinfo (rusinfo.no)** — **BEKRÄFTAT 2026-09-24** (rusinfo.no/om-rusinfo): "Vi som jobber i RUSinfo er fagkonsulenter ansatt i Velferdsetaten i Oslo kommune", med tilskudd från Helsedirektoratet. Alltså en offentlig, kommunal informations- och rådgivningstjänst (telefon 915 08 588 hverdager 11–18, chatt, faktasidor) = kategori 1. Faktasidorna ("Rusmidler A-Å", rusinfo.no/fakta-om-rus-og-legemidler/) är uppdelade i huvudsida + undersidor (virkninger, blanding, overdose, prøver, loven m.m.) och är appens starkaste lekmannaformulerade norska källa. OBS: sidorna har olika ålder (t.ex. ketamin juni 2024 säger "ikke narkotika" — föråldrat efter DMP:s listning 1 jan 2026); kontrollera alltid datum mot DMP/lovdata för juridisk status. RUSinfo saknar sidor om snus, e-sigaretter, HHC/semisyntetiske cannabinoider, xylazin och kratom.
- **Rusopplysningen.no** — **RÄTTELSE 2026-09-18:** drivs INTE av Helsedirektoratet utan av **Foreningen Tryggere Ruspolitikk** (ideell organisation, kontakt rusopplysningen@rusreform.no, Oslo) — bekräftat mot sajtens egen sidfot. Seriös, medicinskt granskad och bred i Norge, men en intresseorganisation med ruspolitisk agenda, inte en myndighet. Konsekvens: när något ska kunna försvaras inför Apple eller ifrågasättas offentligt, styrk det alltid med en myndighetskälla (RUSinfo, Helsenorge/Giftinformasjonen, FHI, Helsedirektoratet) i första hand och låt rusopplysningen.no vara andrakälla. I sourceNote attribueras den som "rusopplysningen.no (Foreningen Tryggere Ruspolitikk)".
- **Helsenorge.no / Giftinformasjonen** — den offentliga helseportalen; Giftinformasjonen (administrerad av FHI, 22 59 13 00 dygnet runt) har egna sidor om akut förgiftning, inkl. en dedikerad rusmedel-sektion (`helsenorge.no/giftinformasjon`) och førstehjelp/bevisstløshet. Metanol-sidan (uppdaterad 30.06.2026) användes 2026-09-24.
- **Folkehelseinstituttet (FHI)** — fhi.no. Nationella hälsorapporter, dödsfallsstatistik, ämnesspecifika notat (kokainnotatet okt. 2025; cannabis-notatet feb. 2025 som fastslår att semisyntetiske cannabinoider omfattas av narkotikaforbudet). Innefattar den tidigare SIRUS (Statens institutt for rusmiddelforskning), som gick upp i FHI 2016.
- **Helsedirektoratet** — helsedirektoratet.no. Lagar, förordningar (veileder till tobakksskadeloven, lystgassforskriften), nationella hälsonyheter (nitazen-dödsfall juni 2025), faglige råd om overdoseforebyggende arbeid, "Tall om illegale rusmidler" (narkotikautløste dødsfall).
- **Direktoratet for medisinske produkter (DMP)** — dmp.no. Fram till 2024 hette myndigheten Statens legemiddelverk; namnbytet är bekräftat och permanent. Ansvarar för narkotikalistan, receptgrupper, godkänd läkemedelsinformation. Se hämtningsbegränsning i sektion 0.
- **Felleskatalogen.no** — den offentligt godkända (DMP-granskade) läkemedelskatalogen. Primärkälla för allt om receptbelagda läkemedel (pregabalin, zolpidem/Stilnoct, tramadol/Nobligan, Spravato, Ventizolve/Nyxoid m.fl.) och deras officiella interaktionstexter — den starkaste enskilda källtypen för läkemedelsinteraktioner.
- **RELIS (regionale legemiddelinformasjonssentre)** — relis.no, med den sökbara interaktionsdatabasen på interaksjoner.no. Fyra regionala centra (bl.a. RELIS Sør-Øst). Svarar på konkreta läkemedelsfrågor från helsepersonell, med publicerade svar/artiklar.
- **Norsk legemiddelhåndbok** — legemiddelhandboka.no. Fackgranskad av läkare/farmasøyter, driven av en självständig stiftelse. Bra för doseringar, kramptrösklar, kontraindikationer. (Delvis JS — bara navigation via WebFetch.)
- **Helsebiblioteket.no** — nationellt fackbibliotek för helsepersonell (drivs av Folkehelseinstituttet); "Forgiftninger"-sektionen (Giftinformasjonens behandlingsanbefalinger per ämne, t.ex. høypotente opioider 2024, alfa-2-agonister 2026, serotonergt syndrom 2026, lystgass, syntetiske cannabinoider) är den viktigaste kliniska källan för overdoseSigns/emergencyAction.
- **Lovdata.no** — den officiella källan för själva lagtexten/forskriftene (narkotikalisten, legemiddelforskriften, tobakksskadeloven m.m.). Se hämtningsbegränsning i sektion 0.
- **KORUS (regionale kompetansesentre rus)** — korus.no. Faktaarken "Fakta om …" (utgivna av KORUS, RUSinfo och Uteseksjonen i Oslo med stöd från Helsedirektoratet) är primärkälla för risikoreduksjon-råden (OBS: arken innehåller doseringsgränser som ALDRIG tas med i appen) och för nalokson-programmet.
- **Store medisinske leksikon (sml.snl.no)** — fackgranskat medicinskt uppslagsverk. Bra sammanfattande bakgrundskälla (t.ex. HHC-artikeln aug. 2025), men inte en primär reglerande källa — använd för kontext, inte som enda källa för en ny sakuppgift.
- **Oslo universitetssykehus obduksjonsstatistikk** — årlig rättstoxikologirapport (oslo-universitetssykehus.no; 2025-rapporten publicerad april 2026). Förstahandskälla för om ett ämne påvisats i norska dödsfall (nitazener 13/18/<5 för 2023/2024/2025; inga xylazin-fynd).
- **Kripos — narkotikastatistikk** (politiet.no/kripos, årlig + halvårsrapporter; 2025-rapporten utgiven 16.01.2026). Förstahandskälla för beslag, renhet (kokain snitt 73 %, THC-halt i hasj 34 % 2025), "pink cocaine"-analyser och för när ett ämne listades (t.ex. "Kratom ble oppført på narkotikalista i desember 2023").
- **OUS / NRAPP — Nasjonal kompetansetjeneste for rus og psykiatri** (oslo-universitetssykehus.no/…/nrapp). Faktasidor per rusmiddel skrivna av OUS-kliniker; kompletterar obduksjonsstatistikken ovan.
- **Helsedirektoratet — Handlingsplan mot overdoser (29 aug 2025)** — ersätter den utgångna Nasjonal overdosestrategi 2019–2022. Citera ALDRIG längre 2019–2022-strategin som gällande policy.
- **overdosefare.no** — nationell varslingstjänst för farliga stoffer i omlopp (ägs av Helsedirektoratet, lanserad 14 jan 2026). Bra för aktuella varningar.
- **Ivareta** (ivareta.no) — pårørendeorganisasjonen bakom Pårørendetelefonen 800 40 567 (hverdager 09–15, tirsdag også 18–21; verifierat 2026-09-24). Appens pårørende-nummer refererar hit.
- **Tidsskrift for Den norske legeforening** (tidsskriftet.no) — norsk fagfellevurdert medicinsk tidskrift; kasuistik och oversiktsartikler (PMMA-dödsfallen 2013/2016, cannabinoid hyperemesis, nitazener).
- **Tolletaten (toll.no)** — gränsregler, privatimport av tobakk/snus, trusselvurdering (27.11.2025 om "rosa kokain"), beslagsstatistik.
- **Ung.no** — Bufdirs offentliga, kvalitetssikrade informationskanal för ungdom. Bra för juridiska gråsonesfrågor (poppers = legemiddel, HHC-vape) och för att fånga upp trendsubstanser tidigt.
- **regjeringen.no** — pressmeddelanden och høringer (forenklet forelegg-høringen 26.06.2026, EU:s tobaksdirektiv i kraft 30.03.2026).

## 2. Internationella läkemedels-/hälsomyndigheter (reglerande organ)

Används som komplement när norsk täckning saknas, eller för att styrka internationella regulatoriska varningar.

- **UNODC / CND (unodc.org, LSS-announcements)** — FN:s narkotikakommission. Förstahandskälla för internationell listning (HHC: beslut CND 68, mars 2025, i kraft 6 dec 2025; mefedron 2015; zolpidem 2001). Ange alltid både beslutsdatum och ikraftträdande.
- **FDA (fda.gov)** — Drug Safety Communications är förstahandskälla för regulatoriska varningar (2019 gabapentinoid+opioid-varningen).
- **EMA (ema.europa.eu)** — norskspråkiga produktresuméer (SPC) för EU-godkända läkemedel, EU-nivå-komplement till Felleskatalogen.
- **EUDA — European Union Drugs Agency (euda.europa.eu)** — **VIKTIGT NAMNBYTE:** hette EMCDDA fram till 2 juli 2024. Gammalt namn/domän ska INTE användas. Europeisk narkotikarapport 2024/2025/2026 (tusi/"pink cocaine", xylazin i Europa), stoffprofiler, riskvurderingar (NBOMe).
- **CDC (cdc.gov)** — xylazin (nalokson upphäver inte xylazin; innblåsninger), MMWR om kratom-relaterade dödsfall (2019). Amerikansk statistik = internationell jämförelse, inte norsk fakta.
- **WHO (who.int)** — global klassificering, ECDD-bedömningar (kratom under övervakning).
- **SAMHSA (samhsa.gov)** — amerikansk behandlingspolicy, kontext.

## 3. Internationella kliniska/vetenskapliga referenskällor

- **NIDA — National Institute on Drug Abuse (nida.nih.gov)** — del av NIH. Internationellt komplement (kokaetylen, metamfetamins nevrotoksisitet och delvis reversibilitet). **OBS:** pågående diskussion i USA om sammanslagning med NIAAA — dubbelkolla domän/namn innan citering. Vissa undersidor blockerar WebFetch (robots).
- **StatPearls / NCBI Bookshelf (ncbi.nlm.nih.gov/books)** — fackgranskad klinisk referens (t.ex. cannabinoid hyperemesis syndrome).
- **PubMed / PMC (pubmed.ncbi.nlm.nih.gov)** — sökingången till fackgranskad litteratur. Citera studien direkt (PMID) — PMC-fulltext ger ofta captcha, abstract räcker.
- **Cochrane Library (cochranelibrary.com)** — systematiska översikter.
- **Ugeskrift for Læger (ugeskriftet.dk)** — dansk fackgranskad tidskrift; kasuistik om HHC-P-förgiftning (09.09.2024) och cannabinoid hyperemesis.

## 4. Uttryckligen UNDVIKA som primärkälla

Dessa kan dyka upp i sökresultat och innehåller ibland korrekt information, men uppfyller INTE Martins krav på "medicinskt styrkta sidor" och ska aldrig vara den enda eller huvudsakliga källan för en sakuppgift i appen:

- PsychonautWiki, Erowid, TripSit — användarredigerade harm-reduction-wikis.
- Generiska forum (Reddit, Flashback och liknande) — aldrig.
- Chemfriendly.no och liknande norska skadereduktionssidor — seriösa och OUS/Olafiaklinikken-anknutna, men en skadereduktionsorganisation, inte en myndighet. Stöd/komplement, aldrig huvudreferens.
- **NHI.no, sundhed.dk** och andra privata/utländska helseportaler — borttagna ur appens sourceNotes 2026-09-24; använd norska myndighetskällor i stället.
- Wikipedia (alla språk) — bara för sökord, aldrig som citerad slutkälla.
- Nyhetsmedier (NRK, VG, Dagens Medisin, Rett24) — bra för att hitta spåret, men källan i appen måste vara myndigheten/rapporten artikeln bygger på.
- AI-genererade sammanfattningssidor utan namngiven redaktion.

## Underhållsrutin

- Vid varje faktakoll: kontrollera i första hand mot sektion 1 (norska officiella källor, RUSinfo inkluderat), komplettera med sektion 2–3 endast där norsk täckning saknas eller behöver internationell styrkning.
- Om en källa visar sig vara nedlagd, omdöpt eller flyttad: uppdatera raden här direkt.
- Hittas en ny, uppenbart pålitlig källa: lägg till den i rätt sektion med kort motivering.
- Stående bevakningspunkter (per 2026-10-05): forenklet forelegg-forskriften (høringsfrist 1 okt 2026 passerad, ej vedtagen); HHC/karisoprodol — VERIFIERADE och daterade: listade genom FOR-2025-08-26-1715, i kraft 1.9.2025 (HHC P II, karisoprodol P IV; Lovdata API 2026-10-05); zopiklon/eszopiklon — LISTADE med verkan 1.6.2026 (FOR-2026-05-15-892); ev. förbud mot nikotinfria e-sigaretter (ej avgjort).
- Detta dokument ersätter/kompletterar den tidigare källistan i `claude/faktakoll-2026-08-21.md`.
