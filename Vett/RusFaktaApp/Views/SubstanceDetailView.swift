import SwiftUI

struct SubstanceDetailView: View {
    let substance: Substance
    /// When true (arrived here via a search mentioning "overdose" etc.),
    /// the view jumps straight to the emergency card instead of the top —
    /// this can be a life-or-death few seconds, so no scrolling required.
    var scrollToOverdose: Bool = false

    private static let overdoseAnchor = "overdose-card"

    var body: some View {
        ScrollViewReader { proxy in
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    header

                    card(title: "Kort om stoffet", icon: "info.circle", text: substance.shortDescription)

                    bulletCard(title: "Virkning", icon: "sparkles", items: substance.effects)
                    bulletCard(title: "Risiko på kort sikt", icon: "exclamationmark.triangle", items: substance.shortTermRisks)
                    bulletCard(title: "Risiko på lang sikt", icon: "hourglass", items: substance.longTermRisks)

                    emergencyCard
                        .id(Self.overdoseAnchor)

                    card(title: "Fare ved blanding med andre stoffer", icon: "arrow.triangle.merge", text: substance.mixingRisks)
                    card(title: "Juridisk status i Norge", icon: "scalemass", text: substance.legalStatus)

                    Text(substance.sourceNote)
                        .font(.caption2)
                        .foregroundStyle(.secondary)
                        .padding(.top, 4)

                    // A single, obvious, full-width button — not a small
                    // icon tucked into a card — so sharing this whole page
                    // (not just the overdose section) is unmistakable and
                    // easy to find no matter where someone stopped reading.
                    fullPageShareButton
                }
                .padding()
            }
            .onAppear {
                guard scrollToOverdose else { return }
                // A short delay lets the ScrollView finish laying out before
                // we jump — scrolling immediately on appear is unreliable.
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation {
                        proxy.scrollTo(Self.overdoseAnchor, anchor: .top)
                    }
                }
            }
        }
        .navigationTitle(substance.name)
        .navigationBarTitleDisplayMode(.large)
    }

    private var header: some View {
        VStack(alignment: .leading, spacing: 6) {
            if !substance.aliases.isEmpty {
                Text("Også kalt: \(substance.aliases.joined(separator: ", "))")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
            HStack(spacing: 8) {
                Label(substance.category, systemImage: "tag")
                Label(substance.riskLevel, systemImage: "gauge.with.dots.needle.67percent")
            }
            .font(.caption)
            .foregroundStyle(.secondary)
        }
    }

    private var emergencyCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Label("Tegn på overdose", systemImage: "waveform.path.ecg")
                .font(.headline)
                .foregroundStyle(.red)
            ForEach(substance.overdoseSigns, id: \.self) { sign in
                bulletRow(sign)
            }
            Divider()
            Label("Hva du skal gjøre", systemImage: "cross.case.fill")
                .font(.headline)
                .foregroundStyle(.red)
            callToActionBox
            Text(substance.emergencyAction)
                .font(.subheadline)

            NavigationLink {
                EmergencyView()
            } label: {
                Text("Se full nødhjelp-guide")
                    .font(.subheadline.weight(.semibold))
            }
            .padding(.top, 4)
        }
        .padding()
        .background(Color.red.opacity(0.08))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    /// The one and only way to share a substance page: a single, obvious,
    /// full-width button at the very bottom that always shares everything —
    /// virkning, risiko, overdose, blanding, juridisk status. An earlier
    /// version had a second, smaller "share just the overdose part" button
    /// near the top, but having two share buttons with two different scopes
    /// was confusing (people expected either one to share the whole page).
    /// One button, one behaviour: it always shares all the information.
    private var fullPageShareButton: some View {
        // An explicit preview avoids ShareLink's default behaviour of
        // asynchronously generating a link/metadata preview before it can
        // show the share sheet — without this, tapping the button could
        // visibly stall for a few seconds before any options appear.
        ShareLink(
            item: fullPageShareText,
            preview: SharePreview("\(substance.name) – Rusinnsikt")
        ) {
            Label("Del info om \(substance.name)", systemImage: "square.and.arrow.up")
                .font(.subheadline.weight(.semibold))
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.indigo)
                .foregroundStyle(.white)
                .clipShape(RoundedRectangle(cornerRadius: 14))
        }
        .padding(.top, 4)
    }

    /// Plain-text version of the entire substance page — virkning, risiko,
    /// overdose, blanding, juridisk status, alt — so someone can send the
    /// full picture to a friend, parent, or helper without them needing to
    /// have the app installed themselves.
    private var fullPageShareText: String {
        var lines: [String] = [substance.name]
        if !substance.aliases.isEmpty {
            lines.append("Også kalt: \(substance.aliases.joined(separator: ", "))")
        }
        lines.append("")
        lines.append(substance.shortDescription)
        lines.append("")
        lines.append("Virkning:")
        lines.append(contentsOf: substance.effects.map { "• \($0)" })
        lines.append("")
        lines.append("Risiko på kort sikt:")
        lines.append(contentsOf: substance.shortTermRisks.map { "• \($0)" })
        lines.append("")
        lines.append("Risiko på lang sikt:")
        lines.append(contentsOf: substance.longTermRisks.map { "• \($0)" })
        lines.append("")
        lines.append("Tegn på overdose:")
        lines.append(contentsOf: substance.overdoseSigns.map { "• \($0)" })
        lines.append("")
        lines.append("Hva du skal gjøre ved overdose:")
        lines.append(substance.emergencyAction)
        lines.append("")
        lines.append("Fare ved blanding med andre stoffer:")
        lines.append(substance.mixingRisks)
        lines.append("")
        lines.append("Juridisk status i Norge:")
        lines.append(substance.legalStatus)
        lines.append("")
        lines.append("Er du i tvil? Ring 113 – uansett.")
        lines.append("")
        lines.append("Delt fra Rusinnsikt-appen — gratis, nøytral rusinformasjon uten konto eller sporing.")
        lines.append("Last ned appen: \(AppLinks.appStoreURL.absoluteString)")
        return lines.joined(separator: "\n")
    }

    /// The single most important message on this whole screen: remove any
    /// hesitation about calling for help. Fear of "bothering" someone or
    /// getting in trouble is a real, documented reason people wait too long —
    /// this box exists to counter that directly, with an encouraging tone
    /// rather than a scary one.
    private var callToActionBox: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("Er du i tvil? Ring 113 – uansett.")
                .font(.headline)
            Text("Det er alltid riktig å ringe. Det er svært sjelden noen får problemer med politiet for å be om hjelp, og helsepersonell har lovpålagt taushetsplikt.")
                .font(.subheadline)

            Link(destination: URL(string: "tel:113")!) {
                Label("Ring 113 nå", systemImage: "phone.fill")
                    .font(.subheadline.weight(.semibold))
            }
            .padding(.top, 2)
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.red)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color.white, lineWidth: 3)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .foregroundStyle(.white)
    }

    private func card(title: String, icon: String, text: String) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Label(title, systemImage: icon).font(.headline)
            Text(text).font(.subheadline)
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    private func bulletCard(title: String, icon: String, items: [String]) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Label(title, systemImage: icon).font(.headline)
            ForEach(items, id: \.self) { item in
                bulletRow(item)
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    private func bulletRow(_ text: String) -> some View {
        HStack(alignment: .top, spacing: 8) {
            Text("•")
            Text(text).font(.subheadline)
            Spacer(minLength: 0)
        }
    }
}

#Preview {
    NavigationStack {
        SubstanceDetailView(substance: DataStore().substances.first!)
            .environmentObject(DataStore())
    }
}
