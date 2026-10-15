# Överlämning – Vett/Rusinnsikt – Faktakoll-automation
Skapad: 2026-10-15 från Cowork-chatten "Vett faktakoll-jobb (schemalagt)"
Se även: claude/faktakoll-log.md i Claude-projektet "App - Vett" (fullständig loggpost för dagens körning)

## Mål
Det veckovisa, schemalagda faktakoll-jobbet (Cowork, headless i molnet) ska hålla `Substances.json` i Rusinnsikt-appen (norsk rusopplysning, 29 ämnen) uppdaterad mot norska myndighetskällor, och applicera verifierade sakfel direkt utan att fråga. Den här överlämningen gäller INTE ett sakfel i appens innehåll (inget hittades denna vecka) – den gäller två öppna driftsfrågor kring automationen själv, som behöver ett beslut av Martin och sedan en åtgärd.

## Nuläge (körning 2026-10-15)
- Klonade `vett-app` (branch master, commit `a572b76`) direkt från GitHub – ingen Mac-anslutning denna gång, helt headless.
- Granskade alla 29 ämnen i `Substances.json` mot rusopplysningen.no, helsedirektoratet.no, dmp.no, rusinfo.no, korus.no, lovdata.no m.fl.
- Kontrollerade särskilt de två kända bevakningspunkterna:
  - **Avkriminalisering/forenklet forelegg** (kokain/amfetamin/metamfetamin m.fl.): fortfarande INTE i kraft. Høringen om forskriften stängde 1 oktober 2026, men inget ikrafttredelsedatum är satt. Appens text ("ulovlig narkotisk stoff") är fortsatt korrekt.
  - **Forbud mot nikotinfrie e-sigaretter:** fortfarande bara ett förslag, inte avgjort (bekräftat mot helsedirektoratet.no, uppdaterad 12 maj 2026).
- **Resultat: inga sakfel hittade, inga ändringar gjorda i Substances.json, inget committat eller pushat.**
- Fullständig loggpost med alla detaljer finns i Claude-projektet "App - Vett" → `claude/faktakoll-log.md` (se "Filer och sökvägar" nedan för varför den ligger där och inte i repot).

## Öppna frågor – i prioritetsordning

### 1. Trasig dokumentationsflytt i repot (osäkert vilken lösning Martin vill ha)
2026-10-05 noterades att innehållet i `claude/faktakoll-log.md` och `claude/kildereferanser-rusmidler.md` (Claude-projektet "App - Vett") skulle flyttas till repot:
- `Utvecklingsanteckningar/Claude_faktakoll-log.md`
- `Utvecklingsanteckningar/Claude_kildereferanser-rusmidler.md`

Men de filerna finns inte i repot (kontrollerat i en färsk klon av master idag). Kvar i Claude-projektet finns bara pekarstubbar som hänvisar till dessa två filer – alltså pekar de på ingenting just nu, och ingen av filerna är sökbar/läsbar någonstans i sin fulla form.

Två alternativ, fråga Martin vilket han vill:
- **(a)** Återskapa filerna i `Utvecklingsanteckningar/` i repot (innehållet måste byggas upp på nytt – det som finns att utgå från är dagens loggpost i `claude/faktakoll-log.md` i Claude-projektet, plus eventuell sessionshistorik Martin har kvar från 2026-10-05) och committa dem.
- **(b)** Flytta tillbaka: låt Claude-projektets `claude/`-filer vara den riktiga sanningen igen, och ta bort/uppdatera pekarstubbarna så de inte hänvisar till tomma repo-sökvägar.

### 2. Push-policykonflikt (Martin måste bestämma, sedan uppdatera det schemalagda jobbet – inte en Claude Code-uppgift)
Repots `CLAUDE.md` säger uttryckligen: *"Claude pushar inte till GitHub; Martin pushar själv från sin Mac."* Skillen `faktakoll-rusinnsikt` säger samma sak.

Men det schemalagda veckojobbet (skapat i Cowork-appen, inte härifrån) instruerar explicit: klona med en dedikerad fine-grained GitHub-token (scope: endast detta repo, Contents: Read/write), applicera verifierade sakfel direkt, **committa och pusha till master automatiskt**.

Detta blev inte en konflikt denna vecka (inget att committa). Men nästa gång jobbet hittar ett sakfel blir frågan verklig: pusha automatiskt, eller bara föreslå och vänta?

Martin behöver bestämma principen. När han har gjort det:
- Om jobbet INTE ska pusha automatiskt längre: det schemalagda jobbets prompt måste uppdateras (via `update_trigger` i Cowork-appen – Claude Code har ingen åtkomst till detta, det måste göras av Martin eller en Cowork-session).
- Om jobbet SKA fortsätta pusha automatiskt: uppdatera `CLAUDE.md` och skillen `faktakoll-rusinnsikt` så de inte längre säger att Claude aldrig pushar, så att framtida sessioner inte får motstridiga instruktioner.

## Filer och sökvägar
- Repo (privat): `https://github.com/8zfm6cz44m-dev/vett-app` (branch `master`)
- Lokalt på Macen (enligt CLAUDE.md, inte verifierat i denna session eftersom ingen Mac-anslutning fanns): sannolikt `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Rusinnsikt`
- Substansinnehåll (ska alltid vara byte-identiska): `Rusinnsikt/RusFaktaApp/Resources/Substances.json` (iOS) och `android/app/src/main/assets/substances.json` (Android)
- `CLAUDE.md` i repot: integritetsregler (noll nätverk, noll tredjepartsberoenden, noll identifierare, etc.) + arbetssätt, inklusive push-regeln ovan
- Claude-projektet "App - Vett": `claude/faktakoll-log.md` (nuvarande faktiska logg – reservlösning tills punkt 1 är löst), `claude/kildereferanser-rusmidler.md` (källbas – samma problem)

## Konventioner (gäller all framtida faktakoll, inte bara denna vecka)
- Ändra ENDAST rena sakfel i `Substances.json` – aldrig design, ordval, ton, struktur. Aldrig Swift-/Kotlin-kod.
- Verifierade sakfel appliceras direkt utan att fråga om godkännande (Martins stående preferens), men kräver minst 2 oberoende källor, eller en primärkälla (Lovdata/myndighet) som otvetydigt motsäger nuvarande text. Vid osäkerhet: ingen ändring, bara en bevakningsnotis i loggen.
- Commit-meddelanden på norska. Footer: `Co-Authored-By: Claude <...>` + `Claude-Session: <url>` enligt den aktuella sessionens instruktion.

## Nästa steg
1. Ta upp punkt 1 (trasig dokumentationsflytt) med Martin direkt i chatten, visa de två alternativen, genomför det han väljer (skapa/committa filerna i repot, eller städa upp pekarna).
2. Ta upp punkt 2 (push-policy) med Martin – detta är ett beslut, inte en kodändring. När han bestämt: om jobbet inte ska pusha, påminn honom om att han (eller en Cowork-session) behöver uppdatera det schemalagda jobbet via `update_trigger` – det går inte att göra från Claude Code på Macen.
3. Inget akut innehållsarbete i övrigt – veckans faktakoll (2026-10-15) gav inga sakfel att rätta i `Substances.json`.

## Beslut 2026-10-15 (Claude Code, "Rusinnsikt forts. #2")
- **Fråga 1 (dokumentationsflytt):** redan löst. Filerna fanns lokalt men låg i 6 opushade commits; GitHub-klonen var inaktuell. Inget återskapas.
- **Fråga 2 (push-policy):** jobbet blir **endast förslagsgivande** – inget commit, ingen push. Se `CLAUDE.md` (avsnitten "Push-policy" och "Verifieringskrav") och `Utvecklingsanteckningar/Claude_veckojobb-prompt.md` (text att lägga in i jobbet via `update_trigger` i Cowork).
- **Verifiering:** primärkälla eller två oberoende källor; juridik via Lovdatas öppna API, inte lovdata.no.
- Skillen `faktakoll-rusinnsikt` är uppdaterad; en referenskopia finns i `Utvecklingsanteckningar/Claude_skill-faktakoll-rusinnsikt.md`.
