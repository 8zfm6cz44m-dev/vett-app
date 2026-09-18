import SwiftUI

/// Always-reachable emergency tab. Kept extremely simple and fast to use —
/// this is the screen someone opens in a panic, so no search, no scrolling
/// past clutter to reach the phone numbers.
struct EmergencyView: View {
    @EnvironmentObject private var store: DataStore

    private var guidance: GeneralEmergencyGuidance { store.database.generalEmergencyGuidance }
    private var numbers: EmergencyNumbers { store.database.emergencyNumbers }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                VStack(alignment: .leading, spacing: 8) {
                    Text(guidance.title)
                        .font(.largeTitle.bold())
                    Text(guidance.goldenRule)
                        .font(.headline)
                        .foregroundStyle(.white)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(Color.red)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                }

                callButtonsRow

                Text(guidance.intro)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)

                guidanceSection(title: "Hvis personen er bevisstløs", icon: "person.fill.xmark", steps: guidance.ifUnconscious, tint: .red)
                guidanceSection(title: "Hvis personen er våken, men svært sløv", icon: "person.fill.questionmark", steps: guidance.ifConsciousButHeavilySedated, tint: .orange)
                guidanceSection(title: "Hvis personen er urolig eller hallusinerer", icon: "waveform.path", steps: guidance.ifAgitatedOrHallucinating, tint: .orange)

                infoBlock(title: "Etterpå", text: guidance.afterCare)
                infoBlock(title: "Om å blande stoffer", text: guidance.mixingPrinciple)

                riskReductionSection

                allNumbersSection
            }
            .padding()
        }
        .navigationTitle("Nødhjelp")
        // Inline (small) instead of the default large title — the content
        // already leads with its own big heading (guidance.title, e.g. "Ved
        // mistanke om overdose"), so a second large system title with
        // different wording directly above it looked like a duplicated/
        // mismatched header. Same fix as TipJarView/AboutView.
        .navigationBarTitleDisplayMode(.inline)
    }

    private var callButtonsRow: some View {
        VStack(spacing: 10) {
            callButton(number: numbers.ambulance)
            callButton(number: numbers.poison)
        }
    }

    @ViewBuilder
    private func callButton(number: EmergencyNumber) -> some View {
        let row = HStack {
            Image(systemName: "phone.fill")
                .accessibilityHidden(true)
            VStack(alignment: .leading) {
                Text(number.label).font(.headline)
                Text(number.number).font(.title3.weight(.bold))
            }
            Spacer()
        }
        .padding()
        .foregroundStyle(.white)
        .background(Color.red)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(number.label), \(number.number)")
        .accessibilityHint("Ring nå")
        .accessibilityAddTraits(.isButton)

        if let url = number.telURL {
            Link(destination: url) { row }
        } else {
            row
        }
    }

    private func guidanceSection(title: String, icon: String, steps: [String], tint: Color) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Label(title, systemImage: icon)
                .font(.headline)
                .foregroundStyle(tint)
            ForEach(Array(steps.enumerated()), id: \.offset) { index, step in
                HStack(alignment: .top, spacing: 10) {
                    Text("\(index + 1)")
                        .font(.caption.bold())
                        .frame(width: 22, height: 22)
                        .background(tint.opacity(0.15))
                        .clipShape(Circle())
                        .accessibilityHidden(true)
                    Text(step).font(.subheadline)
                    Spacer(minLength: 0)
                }
                // Combined so VoiceOver reads "Steg 1: ..." as one step
                // instead of announcing the number circle and the
                // instruction text separately.
                .accessibilityElement(children: .ignore)
                .accessibilityLabel("Steg \(index + 1): \(step)")
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    private func infoBlock(title: String, text: String) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(title).font(.headline)
            Text(text).font(.subheadline).foregroundStyle(.secondary)
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    /// «Hvis noen likevel skal bruke» — helsemyndighetenes generelle råd for
    /// risikoreduksjon. Ikke en nummerert liste: rådene er ikke en rekkefølge.
    private var riskReductionSection: some View {
        let rr = store.database.generalRiskReduction
        return VStack(alignment: .leading, spacing: 10) {
            Label(rr.title, systemImage: "shield.lefthalf.filled")
                .font(.headline)
            Text(rr.intro)
                .font(.subheadline)
                .foregroundStyle(.secondary)
            ForEach(Array(rr.rules.enumerated()), id: \.offset) { _, rule in
                HStack(alignment: .top, spacing: 8) {
                    Text("•")
                    Text(rule).font(.subheadline)
                    Spacer(minLength: 0)
                }
            }
            Text(rr.sourceNote)
                .font(.caption2)
                .foregroundStyle(.secondary)
                .padding(.top, 2)
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    private func phoneRow(_ number: EmergencyNumber) -> some View {
        HStack {
            Text(number.label)
            Spacer()
            Text(number.number).fontWeight(.semibold)
            Image(systemName: "phone")
                .accessibilityHidden(true)
        }
    }

    private var allNumbersSection: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Flere hjelpenumre").font(.headline)
            ForEach(numbers.all) { number in
                if let url = number.telURL {
                    Link(destination: url) {
                        phoneRow(number)
                    }
                    .foregroundStyle(.primary)
                    .accessibilityElement(children: .ignore)
                    .accessibilityLabel("\(number.label), \(number.number)")
                    .accessibilityHint("Ring nå")
                    .accessibilityAddTraits(.isButton)
                } else {
                    // No usable tel: URL — plain text, not announced as a
                    // button since tapping it does nothing.
                    phoneRow(number)
                        .accessibilityElement(children: .ignore)
                        .accessibilityLabel("\(number.label), \(number.number)")
                }
                Divider()
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }
}

#Preview {
    NavigationStack {
        EmergencyView()
            .environmentObject(DataStore())
    }
}
