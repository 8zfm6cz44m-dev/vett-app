import Foundation

/// Central place for the app's App Store link — used both for the
/// "share the app" / "rate us" links in the About screen, and appended to
/// the full-page share text so someone receiving shared substance info can
/// also find and install Rusinnsikt themselves.
///
/// IMPORTANT: `appleID` is a placeholder. Once you've created the app record
/// in App Store Connect (even before it's approved or released), go to
/// App Information → Apple ID and replace the string below with the real
/// numeric ID. You do NOT need to wait for release to get this ID — it's
/// assigned the moment the app record is created.
enum AppLinks {
    static let appleID = "0000000000" // TODO: replace with the real Apple ID from App Store Connect

    static var appStoreURL: URL {
        URL(string: "https://apps.apple.com/no/app/id\(appleID)")!
    }

    /// Deep link that opens straight into the "Write a Review" flow instead
    /// of just the app's product page.
    static var writeReviewURL: URL {
        URL(string: "https://apps.apple.com/no/app/id\(appleID)?action=write-review")!
    }
}
