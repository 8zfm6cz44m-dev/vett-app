import SwiftUI
import StoreKit

/// Optional support screen. Reached only via a single row in "Om" — never
/// shown automatically, never blocks any content.
struct TipJarView: View {
    @StateObject private var store = TipJarStore()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Støtt Rusinnsikt")
                        .font(.largeTitle.bold())
                    Text("Rusinnsikt er gratis for alle og vil alltid forbli det — ingen del av faktainnholdet eller nødhjelp-informasjonen er noensinne bak betaling. Hvis du har mulighet og ønsker å støtte videre utvikling og vedlikehold, kan du gi et frivillig bidrag her.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }

                if store.isLoading {
                    ProgressView().frame(maxWidth: .infinity)
                } else if store.products.isEmpty {
                    Text("Støttealternativer er ikke tilgjengelig akkurat nå.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                } else {
                    VStack(spacing: 12) {
                        ForEach(store.products) { product in
                            Button {
                                Task { await store.purchase(product) }
                            } label: {
                                HStack {
                                    Text(product.displayName)
                                        .font(.headline)
                                    Spacer()
                                    Text(product.displayPrice)
                                        .font(.headline)
                                }
                                .padding()
                                .background(Color(.secondarySystemBackground))
                                .clipShape(RoundedRectangle(cornerRadius: 12))
                            }
                            .foregroundStyle(.primary)
                            .accessibilityElement(children: .ignore)
                            .accessibilityLabel("\(product.displayName), \(product.displayPrice)")
                            .accessibilityAddTraits(.isButton)
                        }
                    }
                }

                if let error = store.errorMessage {
                    Text(error)
                        .font(.footnote)
                        .foregroundStyle(.red)
                }
            }
            .padding()
        }
        // Inline (small) instead of the default large title — the screen
        // already has its own big "Støtt Rusinnsikt" text right at the top, so a
        // second large system title with different wording ("Støtt oss")
        // directly above it looked like a duplicated/mismatched header.
        .navigationTitle("Støtt Rusinnsikt")
        .navigationBarTitleDisplayMode(.inline)
        .alert("Tusen takk!", isPresented: $store.lastThankYou) {
            Button("Bare hyggelig", role: .cancel) {}
        } message: {
            Text("Bidraget ditt hjelper med å holde Rusinnsikt oppdatert og gratis for alle.")
        }
    }
}

#Preview {
    NavigationStack { TipJarView() }
}
