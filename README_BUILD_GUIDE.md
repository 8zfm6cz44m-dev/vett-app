# Rusinnsikt — byggguide

Kort guide för det som inte är helt självförklarande när du bygger och släpper appen.

## Bygga och köra lokalt

1. Öppna `Vett.xcodeproj` i Xcode.
2. Välj schemat **Vett** (target: Vett, host app för testerna: VettTests).
3. Under **Signing & Capabilities**, se till att "Automatically manage signing" är ikryssat och att ditt Apple Developer-team är valt.
4. Cmd+R för att bygga och köra i Simulator eller på enhet.

Bundle ID: `no.rusinnsikt.app`. Visningsnamn: Rusinnsikt.

## Köra testerna

Cmd+U kör hela **VettTests**-målet (FuzzySearch, modell-decoding, JSON-validering, tel:-URL:er).

## Offline-först

Fakta, søk och nødhjelp läses ur `Substances.json` i appen. Inga in-app-köp: App Store IAP kräver nät till Apple. Valfri støtte går via Mail (`mailto:`). Nødnumre öppnar Telefon (`tel:`). Personvernerklæringen ligger i appen.

## App Store Connect

Apple kräver fortfarande en **publik** Privacy Policy-URL i Connect vid inlämning, även om appen visar samma text lokalt. Publicera texten från `PrivacyPolicyView` på en sida du äger när du skickar in. Använd inte Claude-artefakter.

Se `APP_STORE_PREP.md` för review-anteckning och åldersgräns.
