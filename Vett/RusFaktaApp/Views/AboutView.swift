import SwiftUI

struct AboutView: View {
    @EnvironmentObject private var store: DataStore

    private var feedbackMailURL: URL {
        var components = URLComponents()
        components.scheme = "mailto"
        components.path = "elofsson.martin@gmail.com"
        components.queryItems = [
            URLQueryItem(name: "subject", value: "Tilbakemelding om Vett-appen")
        ]
        return components.url!
    }

    var body: some View {
        List {
            Section("Om Vett") {
                Text("Vett er et nøytralt oppslagsverk om rusmidler og risiko, laget for å gi ungdom og andre lettforståelig, faktabasert informasjon — ikke for å oppfordre til bruk. Målet er å forebygge skade og gjøre det enklere å søke hjelp raskt hvis noe går galt.")
                    .font(.subheadline)
            }

            Section("Kilder") {
                Link(destination: URL(string: "https://rusinfo.no")!) {
                    Label("rusinfo.no (Oslo kommune)", systemImage: "link")
                }
                Link(destination: URL(string: "https://rusopplysningen.no")!) {
                    Label("rusopplysningen.no", systemImage: "link")
                }
                Text("Innholdet i appen er skrevet om og forkortet fra disse kildene. Besøk sidene direkte for fullstendig, oppdatert informasjon, chat med rådgiver, eller stoffanalyse.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Section("Personvern") {
                Label("Ingen konto, ingen innlogging", systemImage: "person.crop.circle.badge.xmark")
                Label("Ingen internettilkobling kreves for å bruke appen", systemImage: "wifi.slash")
                Label("Ingen analyse- eller sporingsverktøy", systemImage: "eye.slash")
                Label("Appen lagrer ingenting om deg lokalt", systemImage: "iphone")
            }

            Section {
                NavigationLink {
                    TipJarView()
                } label: {
                    Label("Støtt Vett (valgfritt)", systemImage: "heart")
                }
            } footer: {
                Text("Helt frivillig — appen er og forblir gratis for alle, uansett.")
                    .font(.caption2)
            }

            Section {
                // An explicit preview avoids ShareLink's default behaviour of
                // fetching link-preview metadata over the network before it
                // can show the share sheet — without this, tapping the
                // button could visibly stall for several seconds.
                ShareLink(
                    item: AppLinks.appStoreURL,
                    preview: SharePreview("Vett – Rusinformasjon", image: Image(systemName: "book.closed.fill"))
                ) {
                    Label("Del appen med andre", systemImage: "square.and.arrow.up")
                }
                Link(destination: AppLinks.writeReviewURL) {
                    Label("Vurder Vett i App Store", systemImage: "star")
                }
            } header: {
                Text("Spre appen")
            } footer: {
                Text("Jo flere som vet at Vett finnes, jo flere kan få riktig informasjon i stedet for å google seg fram.")
                    .font(.caption2)
            }

            Section("Hjelpenumre") {
                ForEach(store.database.emergencyNumbers.all) { number in
                    HStack {
                        Text(number.label)
                        Spacer()
                        Text(number.number).foregroundStyle(.secondary)
                    }
                    .accessibilityElement(children: .ignore)
                    .accessibilityLabel("\(number.label), \(number.number)")
                }
            }

            Section {
                Link(destination: feedbackMailURL) {
                    Label("Gi tilbakemelding", systemImage: "envelope")
                }
            } footer: {
                Text("Fant du en feil, eller savner du et stoff eller en funksjon? Send gjerne en e-post.")
                    .font(.caption2)
            }

            Section("Ansvarsfraskrivelse") {
                Text("Vett gir generell, faktabasert informasjon og erstatter ikke profesjonell medisinsk vurdering, akutthjelp eller rådgivning. Innholdet er skrevet om og forkortet fra offentlige kilder og kan inneholde feil eller bli utdatert. Ved mistanke om overdose eller forgiftning: ring alltid 113 eller Giftinformasjonen, uavhengig av hva som står i appen. Vett og appens utvikler er ikke ansvarlig for beslutninger tatt på grunnlag av innholdet.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Section {
                Text("Vett er et uavhengig informasjonsprosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.")
                    .font(.caption2)
                    .foregroundStyle(.secondary)
            }
        }
        .navigationTitle("Om")
    }
}

#Preview {
    NavigationStack {
        AboutView()
            .environmentObject(DataStore())
    }
}
