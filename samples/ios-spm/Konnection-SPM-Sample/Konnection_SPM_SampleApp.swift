//
//  Konnection_SPM_SampleApp.swift
//  Konnection-SPM-Sample
//
//  Created by Magnum Rocha on 17/06/2024.
//

import SwiftUI
import Konnection

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        Konnection.shared.start()
        return true
    }

    func applicationWillTerminate(_ application: UIApplication) {
        Konnection.shared.stop()
    }
}


@main
struct Konnection_SPM_SampleApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
