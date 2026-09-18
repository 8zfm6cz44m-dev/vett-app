import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            NavigationStack {
                SubstanceListView()
            }
            .tabItem { Label("Fakta", systemImage: "book.closed") }

            NavigationStack {
                EmergencyView()
            }
            .tabItem { Label("Nødhjelp", systemImage: "cross.case.fill") }

            NavigationStack {
                SubstanceListView(autoFocusSearch: true, titleOverride: "Søk")
            }
            .tabItem { Label("Søk", systemImage: "magnifyingglass") }

            NavigationStack {
                AboutView()
            }
            .tabItem { Label("Om", systemImage: "info.circle") }
        }
        // Neutral, calm color scheme — deliberately not styled like a
        // "party" or promotional app.
        .tint(.indigo)
    }
}

#Preview {
    ContentView()
        .environmentObject(DataStore())
}
