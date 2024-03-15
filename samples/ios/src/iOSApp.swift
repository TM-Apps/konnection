import SwiftUI
import kotlin

@main
struct iOSApp: App {
    var body: some Scene {
		WindowGroup {
			ContentView()
				.ignoresSafeArea(.all, edges: .bottom)
		}
	}
}
