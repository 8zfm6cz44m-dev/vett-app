# Överlämning – Rusinnsikt – Kodning
Skapad: 2026-10-05 från chatten "App - Rusinnsikt" (Cowork, projekt App - Vett)
Se även: Claude_Overlamning_Planering_20261005.md

## Repo och mappar
- Arbetsmapp: `/Users/martin/Documents/03_Projekt/Claude_appar_och_hemsidor/Rusinnsikt/` (omdöpt från Vett 2026-10-05). Git-remote `origin` = `https://github.com/8zfm6cz44m-dev/vett-app.git` (privat), branch `master`, synkad t.o.m. `c3a9ba1`. Ocommittad ändring: `CURRENT_PROJECT_VERSION 7 → 8` i pbxproj.
- Publik spegel: `~/Developer/rusinnsikt` → `github.com/8zfm6cz44m-dev/rusinnsikt` (branch `main`, en commit, author "Devlyn IT <info@rusinnsikt.no>" – satt med `git config user.name/email` lokalt i den mappen). Uppdateras genom ny export, inte genom merge.
- Tomma rester `Vett/RusFaktaApp/` i projektroten: kan tas bort. `NEXT.md` är Martins egen fil (ospårad), rör inte.
- Repot ligger i iCloud-synkad mapp: vid `mmap failed`/låsfiler → `find .git -type f -exec cat {} + > /dev/null`, ta bort `.git/*.lock*`, `git gc`.

## Struktur
```
Rusinnsikt.xcodeproj/            iOS-projekt (target Rusinnsikt, testmål RusinnsiktTests, scheme Rusinnsikt)
Rusinnsikt/RusFaktaApp/          SwiftUI-kod: RusFaktaApp.swift (PrivacyCover vid scenePhase != .active),
                                 Models.swift, DataStore.swift, FuzzySearch.swift, Views/*.swift,
                                 Resources/Substances.json, PrivacyInfo.xcprivacy
RusinnsiktTests/                 XCTest: SubstanceModelTests, SubstancesJSONTests (dos-regex), FuzzySearchTests
android/                         Kotlin/Compose, applicationId no.rusinnsikt.app, assets/substances.json,
                                 data/Models.kt, ui/screens/*.kt, app/src/test/.../SubstancesJsonTest.kt
docs/                            privacy.html, support-privacy-tekst.txt, personvern-tekst.txt,
                                 devlyn-site/rusinnsikt/ (sidorna som ligger på devlyn.no)
scripts/personvern-sjekk.sh      36 kontroller, alla måste ge PASS före release
CLAUDE.md                        hårda regler (läs först); PERSONVERN_AUDIT.md; APP_STORE_PREP.md; README_BUILD_GUIDE.md
```
Bundle-id `no.rusinnsikt.app`, testmål `no.rusinnsikt.app.tests`, MARKETING_VERSION 1.0, Team-ID finns i pbxproj (aldrig i publika repot).

## Byggkommandon
- Tester: Cmd+U i Xcode (scheme Rusinnsikt). Android: `./gradlew test` i `android/`.
- Release-gate: `bash scripts/personvern-sjekk.sh` (kör i Terminal på Macen efter Product → Archive så att binärkontrollen med nm/otool körs; annars SKIP).
- TestFlight: Clean Build Folder → Archive → Distribute (TestFlight & App Store). Bumpa `CURRENT_PROJECT_VERSION` före varje uppladdning (två förekomster för huvudtarget).
- Claude (Cowork/Code) kan inte bygga Xcode/Gradle och ska inte pusha – Martin kör Cmd+U och `git push`.

## Konventioner
- Apptexter norsk bokmål; chatt svenska. Commits på norska med footer `Co-Authored-By: Claude …` + `Claude-Session: …` när Claude committar.
- `Rusinnsikt/RusFaktaApp/Resources/Substances.json` och `android/app/src/main/assets/substances.json` ska alltid vara **byte-identiska** (`cmp`). Filen har egen formatering (inline-listor, småobjekt på en rad) – `json.dumps` förstör den; ändra med formatbevarande textersättning.
- `riskReduction`-texter och `generalRiskReduction.rules` får aldrig matcha `\d+([.,]\d+)?\s?(mg|ml|g|gram|µg|mikrogram|milligram|milliliter|dl|cl)\b` – tester failar.
- Inga URL:er i Substances.json (personvern-sjekk kontrollerar). Inga nätverks-API:er, inga beroenden (iOS noll SPM; Android bara AndroidX/Compose), inga loggar, ingen `FLAG_SECURE` (Android använder `setRecentsScreenshotEnabled(false)` på API 33+).
- Utvecklare/utgivare: "Devlyn IT". E-post: info@rusinnsikt.no. Martins namn/privata adress får inte finnas i kod, docs eller publika repot.
- Innehållskällor: se projektdoc `claude/kildereferanser-rusmidler.md` (kategori 1–3). rusinfo.no blockerar hämtning – hämta via webbläsaren på Macen. Skillen `faktakoll-rusinnsikt` beskriver hela proceduren.

## Kända buggar / skräp
- Inga kända buggar i appen. Alla tester gröna 25 sept (build 7).
- Skärmbilderna i `App Store Screenshots/` är från 12 sept; Om-skärmen visar inte "utviklet av Devlyn IT".
- `Vett/RusFaktaApp/` tomma mappar i roten.

## Pågående uppgift / nästa kodsteg
1. `git add Rusinnsikt.xcodeproj/project.pbxproj && git commit -m "Build 8"` – efter att Martin bekräftat om build 8 laddats upp (annars låt stå).
2. Ta bort tomma `Vett/`-mappar, committa.
3. Vid nästa innehållsändring: uppdatera båda JSON, kör dos-regex + `cmp` + personvern-sjekk, Cmd+U, bumpa build.
4. Spegling till publika repot vid release:
   ```
   git archive HEAD | tar -x -C ~/Developer/rusinnsikt    # i arbetsmappen
   cd ~/Developer/rusinnsikt
   rm -rf Utvecklingsanteckningar android/Utvecklingsanteckningar APP_STORE_PREP.md
   sed -i '' 's/DEVELOPMENT_TEAM = [A-Z0-9]*;/DEVELOPMENT_TEAM = "";/' Rusinnsikt.xcodeproj/project.pbxproj
   grep -r -i -E 'elofsson|martin|gmail' . && echo STOPP   # ska vara tomt
   git add -A && git commit -m "Rusinnsikt 1.0 (N)" && git push
   ```
   Kontrollera även CLAUDE.md (ägarrad) och PERSONVERN_AUDIT.md ("Till utgivaren") efter export; README.md/LICENSE i spegeln får inte skrivas över av exporten (de finns inte i vett-app – lägg dem i vett-app om det blir krångligt).
