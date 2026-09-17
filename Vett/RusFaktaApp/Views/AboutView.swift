import SwiftUI

struct AboutView: View {
    @EnvironmentObject private var store: DataStore
    @AppStorage(OnboardingState.hasSeenOnboardingKey) private var hasSeenOnboarding = false

    private var feedbackMailURL: URL {
        var components = URLComponents()
        components.scheme = "mailto"
        components.path = "info@rusinnsikt.no"
        components.queryItems = [
            URLQueryItem(name: "subject", value: "Tilbakemelding om Rusinnsikt-appen")
        ]
        return components.url!
    }

    private var shareAppText: String {
        """
        Rusinnsikt er et nøytralt oppslagsverk om rusmidler og risiko — gratis, uten konto og uten sporing. Fakta og nødhjelp ligger i appen.

        Ved mistanke om overdose: ring 113.
        """
    }

    var body: some View {
        List {
            Section {
                Text("Om Rusinnsikt")
                    .font(.largeTitle.bold())
                    .listRowSeparator(.hidden)
                Text("Rusinnsikt er et nøytralt oppslagsverk om rusmidler og risiko, laget for å gi ungdom og andre lettforståelig, faktabasert informasjon — ikke for å oppfordre til bruk. Målet er å forebygge skade og gjøre det enklere å søke hjelp raskt hvis noe går galt.")
                    .font(.subheadline)
            }

            Section("Kilder") {
                Label("rusinfo.no (Oslo kommune)", systemImage: "building.columns")
                Label("rusopplysningen.no", systemImage: "building.columns")
                Text("Innholdet i appen er skrevet om og forkortet fra disse kildene og ligger ferdig i appen. Appen åpner ikke nettstedene. For chat, stoffanalyse eller fullstendig oppdatert tekst må du selv slå opp kildene i en nettleser.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Section("Personvern") {
                Label("Ingen konto, ingen innlogging", systemImage: "person.crop.circle.badge.xmark")
                Label("Fakta, søk og nødhjelp krever ikke internett", systemImage: "wifi.slash")
                Label("Ingen analyse- eller sporingsverktøy", systemImage: "eye.slash")
                Label("Lokalt lagres kun om du har sett velkomstskjermen", systemImage: "iphone")
                NavigationLink {
                    PrivacyPolicyView()
                } label: {
                    Label("Full personvernerklæring", systemImage: "doc.text")
                }
            }

            Section {
                NavigationLink {
                    TipJarView()
                } label: {
                    Label("Støtt Rusinnsikt (valgfritt)", systemImage: "heart")
                }
            } footer: {
                Text("Helt frivillig — appen er og forblir gratis. Ingen kjøp i appen.")
                    .font(.caption2)
            }

            Section {
                ShareLink(
                    item: shareAppText,
                    preview: SharePreview("Rusinnsikt – rusinformasjon")
                ) {
                    Label("Del appen med andre", systemImage: "square.and.arrow.up")
                }
            } header: {
                Text("Spre appen")
            } footer: {
                Text("Deler vanlig tekst — ingen App Store-lenke og ingen nettforespørsel.")
                    .font(.caption2)
            }

            Section("Hjelpenumre") {
                ForEach(store.database.emergencyNumbers.all) { number in
                    if let url = number.telURL {
                        Link(destination: url) {
                            HStack {
                                Text(number.label)
                                Spacer()
                                Text(number.number).foregroundStyle(.secondary)
                            }
                        }
                        .foregroundStyle(.primary)
                        .accessibilityElement(children: .ignore)
                        .accessibilityLabel("\(number.label), \(number.number)")
                        .accessibilityHint("Ring nå")
                        .accessibilityAddTraits(.isButton)
                    } else {
                        HStack {
                            Text(number.label)
                            Spacer()
                            Text(number.number).foregroundStyle(.secondary)
                        }
                        .accessibilityElement(children: .ignore)
                        .accessibilityLabel("\(number.label), \(number.number)")
                    }
                }
            }

            Section {
                Link(destination: feedbackMailURL) {
                    Label("Gi tilbakemelding", systemImage: "envelope")
                }
            } footer: {
                Text("Åpner Mail-appen. Ingenting sendes før du selv trykker send.")
                    .font(.caption2)
            }

            Section {
                Button("Nullstill velkomstskjerm") {
                    store.clearAllLocalData()
                    hasSeenOnboarding = false
                }
            } footer: {
                Text("Sletter den ene lokale innstillingen. Velkomstskjermen vises neste gang du åpner appen.")
                    .font(.caption2)
            }

            Section("Ansvarsfraskrivelse") {
                Text("Rusinnsikt gir generell, faktabasert informasjon og erstatter ikke profesjonell medisinsk vurdering, akutthjelp eller rådgivning. Innholdet er skrevet om og forkortet fra offentlige kilder og kan inneholde feil eller bli utdatert. Ved mistanke om overdose eller forgiftning: ring alltid 113 eller Giftinformasjonen, uavhengig av hva som står i appen. Rusinnsikt og appens utvikler er ikke ansvarlig for beslutninger tatt på grunnlag av innholdet.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Section {
                Text("Rusinnsikt er et uavhengig informasjonsprosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.")
                    .font(.caption2)
                    .foregroundStyle(.secondary)
            }
        }
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack {
        AboutView()
            .environmentObject(DataStore())
    }
}
