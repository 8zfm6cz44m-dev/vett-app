import StoreKit
import Combine
import Foundation

/// A small, entirely optional "support the developer" tip jar.
///
/// Deliberately NOT used for anything content-related: every substance page,
/// the emergency guidance, and the helpline numbers work fully without ever
/// opening this screen or spending a krone. This only unlocks a short
/// "thank you" state in the UI — nothing safety-related is ever gated
/// behind payment.
///
/// Product IDs below match the app's real bundle identifier
/// (com.app.vett) — create matching consumable in-app purchases in App
/// Store Connect with these exact identifiers. See README_BUILD_GUIDE.md
/// step "Donasjoner / tip jar" for the App Store Connect setup steps, and
/// use Rusinnsikt.storekit to test purchases locally before you've configured
/// anything in App Store Connect. Not wired into the scheme by default —
/// enable it via Xcode: Product > Scheme > Edit Scheme > Run > Options >
/// StoreKit Configuration > Rusinnsikt.storekit.
@MainActor
final class TipJarStore: ObservableObject {
    static let productIDs = [
        "com.app.vett.tip.small",   // e.g. 19 kr
        "com.app.vett.tip.medium",  // e.g. 49 kr
        "com.app.vett.tip.large"    // e.g. 99 kr
    ]

    @Published private(set) var products: [Product] = []
    @Published private(set) var isLoading = false
    @Published var lastThankYou: Bool = false
    @Published var errorMessage: String?

    private var transactionListener: Task<Void, Never>?

    init() {
        transactionListener = listenForTransactions()
        Task { await loadProducts() }
    }

    deinit {
        transactionListener?.cancel()
    }

    func loadProducts() async {
        isLoading = true
        defer { isLoading = false }
        do {
            let fetched = try await Product.products(for: Self.productIDs)
            products = fetched.sorted { $0.price < $1.price }
        } catch {
            errorMessage = "Klarte ikke å hente støttealternativer. Sjekk internettforbindelsen."
        }
    }

    func purchase(_ product: Product) async {
        do {
            let result = try await product.purchase()
            switch result {
            case .success(let verification):
                if case .verified(let transaction) = verification {
                    await transaction.finish()
                    lastThankYou = true
                }
            case .userCancelled, .pending:
                break
            @unknown default:
                break
            }
        } catch {
            errorMessage = "Kjøpet kunne ikke fullføres. Prøv igjen senere."
        }
    }

    private func listenForTransactions() -> Task<Void, Never> {
        Task.detached { [weak self] in
            for await update in Transaction.updates {
                if case .verified(let transaction) = update {
                    await transaction.finish()
                    // Awaiting an optional-chained call straight into a @MainActor
                    // method (rather than capturing `self` again inside a nested
                    // MainActor.run { } closure) is what Swift 6 strict
                    // concurrency wants here: reading a weakly-captured `self`
                    // from inside a second, separately-concurrently-executing
                    // closure is exactly the "reference to captured var 'self'
                    // in concurrently-executing code" error the old version hit.
                    await self?.markThankYou()
                }
            }
        }
    }

    @MainActor
    private func markThankYou() {
        lastThankYou = true
    }
}
