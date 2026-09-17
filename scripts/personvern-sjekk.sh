#!/usr/bin/env bash
# Rusinnsikt — mekanisk personvernkontroll. Kör före varje release:
#   bash scripts/personvern-sjekk.sh
# Avslutar med felkod om något av de absoluta kraven i CLAUDE.md bryts.
set -u
cd "$(dirname "$0")/.."
IOS="Vett/RusFaktaApp"; AND="android/app/src/main"; PBX="Vett.xcodeproj/project.pbxproj"
fail=0
ok(){ printf '  PASS  %s\n' "$1"; }
bad(){ printf '  FAIL  %s\n' "$1"; fail=1; }
none(){ # none <beskrivning> <regex> <paths...>
  local d="$1" re="$2"; shift 2
  if grep -rnE "$re" "$@" >/dev/null 2>&1; then bad "$d"; grep -rnE "$re" "$@" | head -5 | sed 's/^/          /'; else ok "$d"; fi
}
SRC_IOS=$(find "$IOS" -name '*.swift'); SRC_AND=$(find "$AND" -name '*.kt')

echo "== Nätverk =="
none "Inga nätverks-API:er i Swift" "URLSession|URLRequest|dataTask|WKWebView|SFSafariViewController|NWConnection|Alamofire" $SRC_IOS
none "Inga nätverks-API:er i Kotlin" "OkHttp|Retrofit|HttpURLConnection|WebView|Ktor|Volley|openConnection|java\.net\.Socket|Coil|Glide" $SRC_AND
none "Ingen INTERNET-behörighet i Android-manifest" "android\.permission\.INTERNET|ACCESS_NETWORK_STATE" "$AND/AndroidManifest.xml"
none "Inga http(s)-URL:er i källkod" "https?://" $SRC_IOS $SRC_AND
none "Ingen ATS-konfiguration (skulle bara behövas för nätverk)" "NSAppTransportSecurity|NSAllowsArbitraryLoads" "$PBX"

echo "== Beroenden =="
none "Inga Swift-paket i Xcode-projektet" "XCRemoteSwiftPackageReference|XCLocalSwiftPackageReference" "$PBX"
if [ -z "$(find . -maxdepth 2 \( -name Podfile -o -name Cartfile -o -name Package.swift \) -not -path './android/*')" ]; then ok "Ingen Podfile/Cartfile/Package.swift"; else bad "Pakethanterarfil hittad"; fi
if grep -E 'group *= *"' android/gradle/libs.versions.toml | grep -vE 'group *= *"(androidx\.|junit"|org\.json")' >/dev/null; then bad "Okänd beroendegrupp i libs.versions.toml"; grep -E 'group *= *"' android/gradle/libs.versions.toml | grep -vE 'group *= *"(androidx\.|junit"|org\.json")' | sed 's/^/          /'; else ok "Endast androidx / junit / org.json i libs.versions.toml"; fi
none "Inga analys-/annons-/crash-SDK:er" "Firebase|Crashlytics|Sentry|Mixpanel|Amplitude|Bugsnag|AppCenter|AdMob|OneSignal|Braze|Segment|Flurry|GoogleService|google-services" $SRC_IOS $SRC_AND android/app/build.gradle.kts android/gradle/libs.versions.toml "$PBX"

echo "== Identifierare & behörigheter =="
none "Inga enhets-/reklamidentifierare" "identifierForVendor|advertisingIdentifier|ASIdentifierManager|ATTrackingManager|AppTrackingTransparency|AdvertisingIdClient|ANDROID_ID|Settings\.Secure" $SRC_IOS $SRC_AND
none "Inga uses-permission i Android-manifest" "<uses-permission" "$AND/AndroidManifest.xml"
none "Inga NS*UsageDescription-nycklar (iOS)" "UsageDescription" "$PBX"
if [ -z "$(find . -name '*.entitlements' -not -path '*/Build/*')" ]; then ok "Ingen entitlements-fil (ingen iCloud/CloudKit/App Groups/Push)"; else bad "Entitlements-fil hittad"; fi

echo "== Lagring =="
none "Inga andra UserDefaults-nycklar än hasSeenOnboarding (iOS)" "UserDefaults\.standard\.set\(|\.set\([^)]*forKey|AppStorage\(\"(?!hasSeenOnboarding)" $SRC_IOS
if grep -rnE 'AppStorage\(' $SRC_IOS | grep -v 'OnboardingState.hasSeenOnboardingKey' >/dev/null; then bad "AppStorage med annan nyckel än onboarding-flaggan"; else ok "AppStorage används bara för onboarding-flaggan"; fi
none "Ingen filskrivning/databas/Keychain (iOS)" "FileManager\.default\.(createFile|write)|\.write\(to|CoreData|SwiftData|NSPersistent|SecItemAdd|Keychain|NSUbiquitous|CloudKit" $SRC_IOS
none "Ingen filskrivning/databas (Android)" "openFileOutput|filesDir|cacheDir|externalCacheDir|getExternalFilesDir|Room|SQLiteDatabase|DataStore<|EncryptedSharedPreferences" $SRC_AND
if grep -rnE 'putString|putInt|putLong|putFloat|putStringSet' $SRC_AND >/dev/null; then bad "SharedPreferences lagrar mer än en boolean"; else ok "SharedPreferences: bara putBoolean(hasSeenOnboarding)"; fi
none "Sökfältet persisteras inte" "searchText.*AppStorage|AppStorage.*search|rememberSaveable.*query.*Preferences" $SRC_IOS $SRC_AND

echo "== Backup (Android) =="
grep -q 'android:allowBackup="false"' "$AND/AndroidManifest.xml" && ok "allowBackup=false" || bad "allowBackup är inte false"
for mode in cloud-backup device-transfer; do
  if awk "/<$mode/,/<\/$mode>/" "$AND/res/xml/data_extraction_rules.xml" | grep -q 'exclude domain="sharedpref"'; then ok "data_extraction_rules: sharedpref exkluderat i $mode"; else bad "data_extraction_rules saknar sharedpref-exclude i $mode"; fi
done
grep -q 'exclude domain="sharedpref"' "$AND/res/xml/backup_rules.xml" && ok "backup_rules (API<31): sharedpref exkluderat" || bad "backup_rules saknar exclude"

echo "== Loggning, systemintegrationer, app-växlare =="
none "Ingen loggning i produktionskod" "(^|[^a-zA-Z])print\(|NSLog\(|os_log|Logger\(|debugPrint\(|Log\.(d|e|i|v|w|wtf)\(|println\(|Timber\." $SRC_IOS $SRC_AND
none "Ingen Spotlight/App Intents/Siri/Widgets/Handoff" "CoreSpotlight|CSSearchable|NSUserActivity|AppIntent|INIntent|WidgetKit|ActivityKit|Handoff" $SRC_IOS $SRC_AND "$PBX"
none "Inga notiser/bakgrundslägen/push" "UNUserNotificationCenter|BGTaskScheduler|registerForRemoteNotifications|UIBackgroundModes|WorkManager|AlarmManager|FirebaseMessaging|NotificationCompat" $SRC_IOS $SRC_AND "$PBX" "$AND/AndroidManifest.xml"
none "Inga URL-scheman/deep links" "CFBundleURLTypes|CFBundleURLSchemes|associated-domains|onOpenURL|android:scheme|BROWSABLE|autoVerify" $SRC_IOS "$PBX" "$AND/AndroidManifest.xml"
none "Ingen StoreKit/Play Billing" "StoreKit|SKPayment|BillingClient|com\.android\.billing" $SRC_IOS $SRC_AND android/app/build.gradle.kts
grep -q 'PrivacyCover' "$IOS/RusFaktaApp.swift" && ok "iOS: PrivacyCover finns i RusFaktaApp.swift" || bad "iOS: PrivacyCover saknas"
grep -q 'setRecentsScreenshotEnabled(false)' "$AND/java/no/rusinnsikt/app/MainActivity.kt" && ok "Android: setRecentsScreenshotEnabled(false) finns" || bad "Android: setRecentsScreenshotEnabled saknas"
none "FLAG_SECURE används inte (skulle blockera skärmdumpar av nödhjälp)" "^[[:space:]]*[^[:space:]/].*FLAG_SECURE" $SRC_AND

echo "== Privacy manifest & innehåll =="
grep -A1 'NSPrivacyTracking</key>' "$IOS/PrivacyInfo.xcprivacy" | grep -q '<false/>' && ok "PrivacyInfo: NSPrivacyTracking=false" || bad "PrivacyInfo: NSPrivacyTracking är inte false"
grep -A1 'NSPrivacyCollectedDataTypes' "$IOS/PrivacyInfo.xcprivacy" | grep -q '<array/>' && ok "PrivacyInfo: inga insamlade datatyper" || bad "PrivacyInfo: insamlade datatyper deklarerade"
cmp -s "$IOS/Resources/Substances.json" "$AND/assets/substances.json" && ok "Substances.json identisk iOS/Android" || bad "Substances.json skiljer sig mellan plattformarna"
none "Inga URL:er i innehållet" "https?://" "$IOS/Resources/Substances.json"

echo
if [ $fail -eq 0 ]; then echo "ALLA KONTROLLER GODKÄNDA"; else echo "MINST EN KONTROLL UNDERKÄND — släpp inte"; fi
exit $fail
