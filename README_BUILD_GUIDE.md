# Vett — byggguide

Kort guide för det som inte är helt självförklarande när du bygger och släpper appen.

## Bygga och köra lokalt

1. Öppna `Vett.xcodeproj` i Xcode.
2. Välj schemat **Vett** (target: Vett, host app för testerna: VettTests).
3. Under **Signing & Capabilities**, se till att "Automatically manage signing" är ikryssat och att ditt Apple Developer-team är valt.
4. Cmd+R för att bygga och köra i Simulator eller på enhet.

## Köra testerna

Cmd+U kör hela **VettTests**-målet (FuzzySearch, modell-decoding, JSON-validering).

## Donasjoner / tip jar

`TipJarStore.swift` innehåller tre produkt-ID:n för en frivillig "tip jar" (ingen funktionalitet i appen är låst bakom dessa):

```
com.app.vett.tip.small
com.app.vett.tip.medium
com.app.vett.tip.large
```

**Innan tip jar fungerar skarpt behöver du:**

1. Ett Apple Developer-konto och en app-post i App Store Connect (bundle ID `com.app.vett`).
2. Skapa tre **Consumable**-produkter i App Store Connect under appens "In-App Purchases", med exakt ovanstående produkt-ID:n, och sätt pris (t.ex. 19/49/99 kr).
3. Vänta tills Apple godkänt produkterna (kan ta ett tag första gången) innan de går att hämta i appen.

**Testa lokalt utan App Store Connect:** använd `Vett.storekit` (StoreKit-konfigurationsfil, redan i repot med samma tre produkt-ID:n och testpriser). Den är inte kopplad till schemat som standard — aktivera den via:

Xcode → Product → Scheme → Edit Scheme… → Run → Options-fliken → StoreKit Configuration → välj `Vett.storekit`.

Med det aktiverat simulerar Xcode köp lokalt utan att röra riktiga App Store Connect-produkter eller riktiga pengar.

## App Store Connect — release-checklista

Se projektets `claude/code-review-2026-08-21.md` (i Claude-projektet "App - Vett") för en fullständig, uppdaterad lista över vad som återstår innan release.
