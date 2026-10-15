# Veckojobbet faktakoll – ny prompttext (beslutad 2026-10-15)

Läggs in av Martin eller en Cowork-session i det schemalagda jobbet via `update_trigger`. Claude Code kan inte ändra jobbet.

Efter ändringen behöver jobbet bara **läsbehörighet** till GitHub. Den fine-grained token som har Contents: Read/write kan ersättas med en som har Contents: Read-only (görs av Martin på github.com).

---

Kör skillen `faktakoll-rusinnsikt` i **schemalagt/headless läge** mot Rusinnsikt-appen.

1. Klona `8zfm6cz44m-dev/vett-app` (branch `master`). Läs `CLAUDE.md` (särskilt "Push-policy" och "Verifieringskrav"), `Utvecklingsanteckningar/Claude_kildereferanser-rusmidler.md` och de senaste posterna i `Utvecklingsanteckningar/Claude_faktakoll-log.md`. Ange granskad commit (`git rev-parse --short HEAD`). Om den senaste loggposten beskriver ändringar som inte finns i klonen: skriv det överst i rapporten (Martin har troligen inte pushat).
2. Kontrollera att `Rusinnsikt/RusFaktaApp/Resources/Substances.json` och `android/app/src/main/assets/substances.json` är identiska (`cmp`).
3. Granska alla ämnen och generella sektioner enligt skillen. Juridisk status verifieras mot Lovdatas öppna API (`https://api.lovdata.no/v1/publicData/list` → `/v1/publicData/get/<fil>`; endast `gjeldende-sentrale-forskrifter.tar.bz2` och `lovtidend-avd1-<innevarande år>.tar.bz2`), aldrig mot lovdata.no.
4. Ett förslag är **verifierat** bara om en primärkälla (Lovdata, norsk myndighet, RUSinfo/Helsedirektoratet, FHI, DMP) otvetydigt motsäger nuvarande text, eller om minst två oberoende källor ur kategori 1–3 gör det. Annars: bevakningsnotis. `legalStatus`, dödsfallssiffror och akut-/nödråd markeras alltid "kräver Martins granskning". `riskReduction` får aldrig innehålla doser eller mängder.
5. **Committa inte och pusha inte. Ändra inga filer i repot.** Leverera i stället en rapport på svenska med: (a) granskad commit, (b) verifierade förslag som JSON `{id, field, old, new, reason, sources[], confidence}`, (c) bevakningsnotiser, (d) en färdig loggpost att klistra in i `Claude_faktakoll-log.md`.
6. Bevaka särskilt: forenklet forelegg-forskriften (vedtak/ikraftträdande) och ett eventuellt förbud mot nikotinfria e-sigaretter.
