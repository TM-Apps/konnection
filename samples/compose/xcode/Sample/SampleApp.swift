//
//  SampleApp.swift
//  Sample
//
//  Created by Magnum Rocha on 18/05/2025.
//

import SwiftUI
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func applicationWillTerminate(_ application: UIApplication) {
        App_iosKt.stopKonnection()
    }
}

@main
struct SampleApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all, edges: .bottom)
        }
    }
}
