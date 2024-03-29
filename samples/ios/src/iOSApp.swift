import UIKit
import SwiftUI
import kotlin

class AppDelegate: NSObject, UIApplicationDelegate {
	func applicationWillTerminate(_ application: UIApplication) {
		MainKt.stop()
	}
}

@main
struct iOSApp: App {
	@UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
		WindowGroup {
			ContentView()
				.ignoresSafeArea(.all, edges: .bottom)
		}
	}
}

struct ContentView: View {
	var body: some View {
		ComposeView()
			.ignoresSafeArea(.keyboard) // Compose has own keyboard handler
	}
}

struct ComposeView: UIViewControllerRepresentable {
	func makeUIViewController(context: Context) -> UIViewController {
		MainKt.MainViewController()
	}

	func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
