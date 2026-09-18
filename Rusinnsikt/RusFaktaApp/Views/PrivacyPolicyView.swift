import SwiftUI

/// Full personvernerklæring som ligger i appen. Ingen nettforespørsel.
struct PrivacyPolicyView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("Personvernerklæring")
                    .font(.largeTitle.bold())

                Text("Sist oppdatert 6. september 2026.")
                    .font(.caption)
                    .foregroundStyle(.secondary)

                Text("Rusinnsikt er laget for å virke helt uten internett. All faktainformasjon, nødhjelp og søk ligger i appen. Vi samler ikke inn personopplysninger, har ingen konto og bruker ingen analyse- eller reklameverktøy.")

                section("Hva appen lagrer", "Én lokal innstilling: om du har sett velkomstskjermen. Den ligger i telefonens vanlige innstillinger (UserDefaults) og sendes aldri noe sted. Du kan slette den under Om → Nullstill velkomstskjerm.")

                section("Hva appen ikke gjør", "Ingen innlogging, ingen sky, ingen sporing, ingen tredjeparts SDK-er for statistikk. Appen henter ikke innhold fra internett når du leser fakta eller nødhjelp.")

                section("Telefon og e-post", "Nødnumre åpner Telefon-appen (`tel:`). Tilbakemelding åpner Mail-appen (`mailto:`). Selve samtalen eller e-posten går via operatør eller e-postleverandør først når du sender eller ringer — det er utenfor Rusinnsikt.")

                section("Kilder", "Teksten er forkortet fra offentlige kilder (rusinfo.no / Oslo kommune og rusopplysningen.no). Appen åpner ikke disse nettstedene. For fullstendig og oppdatert informasjon, chat eller stoffanalyse må du selv åpne kildene i en nettleser.")

                section("Kontakt", "Spørsmål om personvern: info@rusinnsikt.no")

                Text("Rusinnsikt er et uavhengig prosjekt og er ikke offisielt tilknyttet rusinfo.no, rusopplysningen.no, Oslo kommune eller Helsedirektoratet.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            .padding()
        }
        .navigationTitle("Personvern")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func section(_ title: String, _ body: String) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(title).font(.headline)
            Text(body).font(.subheadline)
        }
    }
}

#Preview {
    NavigationStack { PrivacyPolicyView() }
}
