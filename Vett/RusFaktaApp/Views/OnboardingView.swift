import SwiftUI

/// First-launch screen. Sets clear expectations, consistent with an app
/// review team expecting a neutral, non-promotional reference tool
/// (similar in spirit to a pharmaceutical reference app).
struct OnboardingView: View {
    @Binding var hasSeenOnboarding: Bool

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Image(systemName: "book.closed.fill")
                    .font(.system(size: 44))
                    .foregroundStyle(.indigo)
                    .padding(.top, 24)
                    .accessibilityHidden(true)

                Text("Velkommen til Vett")
                    .font(.largeTitle.bold())

                Text("Vett er et faktaoppslagsverk om rusmidler og risiko — på samme måte som en legemiddelkatalog. Appen er laget for å gi korrekt, nøytral informasjon, ikke for å oppfordre til bruk.")
                    .font(.body)

                infoRow(icon: "checkmark.shield", text: "Innholdet er basert på informasjon fra rusinfo.no (Oslo kommune) og rusopplysningen.no.")
                infoRow(icon: "exclamationmark.triangle", text: "Målet er å forebygge skader og redde liv — ikke å oppfordre til rusbruk.")
                infoRow(icon: "lock.shield", text: "Appen krever ingen konto, sender ingen data noe sted, og lagrer ingenting i skyen. Alt skjer lokalt på telefonen din.")
                infoRow(icon: "person.crop.circle.badge.exclamationmark", text: "Innholdet omhandler rus og overdoserisiko og er beregnet for personer over 17 år.")

                Spacer(minLength: 12)

                Button {
                    hasSeenOnboarding = true
                } label: {
                    Text("Jeg forstår, fortsett")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.indigo)
                        .foregroundStyle(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                }
                .padding(.top, 8)
            }
            .padding()
        }
    }

    private func infoRow(icon: String, text: String) -> some View {
        HStack(alignment: .top, spacing: 12) {
            Image(systemName: icon)
                .foregroundStyle(.indigo)
                .frame(width: 24)
                .accessibilityHidden(true)
            Text(text)
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
    }
}

#Preview {
    OnboardingView(hasSeenOnboarding: .constant(false))
}
