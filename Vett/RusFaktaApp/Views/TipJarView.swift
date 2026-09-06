import SwiftUI

/// Valgfri støtte — uten App Store-kjøp. In-app-kjøp krever Apples nett
/// og er bevisst ikke med, så appen kan brukes helt uten internett.
struct TipJarView: View {
    private var supportMailURL: URL {
        var components = URLComponents()
        components.scheme = "mailto"
        components.path = "elofsson.martin@gmail.com"
        components.queryItems = [
            URLQueryItem(name: "subject", value: "Støtte til Rusinnsikt")
        ]
        return components.url!
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Støtt Rusinnsikt")
                        .font(.largeTitle.bold())
                    Text("Rusinnsikt er gratis og skal virke uten internett — fakta, søk og nødhjelp er aldri bak betaling. Derfor er det ingen kjøp i appen (det ville krevd tilkobling til App Store).")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                    Text("Vil du likevel bidra til vedlikehold, send en e-post. Det åpner Mail på telefonen; ingenting sendes før du selv trykker send.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }

                Link(destination: supportMailURL) {
                    Label("Send e-post om støtte", systemImage: "envelope")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.indigo)
                        .foregroundStyle(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                }
            }
            .padding()
        }
        .navigationTitle("Støtt Rusinnsikt")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack { TipJarView() }
}
