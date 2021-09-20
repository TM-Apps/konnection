//
//  ContentView.swift
//  Sample
//
//  Created by TMApps on 29/12/2020.
//

import SwiftUI
import Konnection

struct ContentView: View {
    @State var connection: NetworkConnection? = nil
    @State var ipv4: String? = nil
    @State var ipv6: String? = nil

    private let konnection = KonnectionWrapper()

    var body: some View {
        ZStack {
            Color(hex: 0x4f5b66)

            VStack {
                Image(connection.icon)
                    .resizable()
                    .scaledToFit()
                    .foregroundColor(.white)
                    .frame(width: 150.0, height: 150.0)
                    .padding()

                Text(connection.description)
                    .foregroundColor(.white)
                    .font(.title2)
                    .bold()
                    .padding([.bottom, .horizontal])

                Text(ipv4 ?? "")
                    .foregroundColor(.white)
                    .font(.title3)
                    .bold()
                    .padding([.bottom, .horizontal])

                Text(ipv6 ?? "")
                    .foregroundColor(.white)
                    .font(.title3)
                    .bold()
                    .padding([.bottom, .horizontal])
            }
        }
        .onAppear { onViewAppear() }
    }

    private func onViewAppear() {
        konnection.networkConnectionObservation() { [self] networkConnection in
            self.connection = networkConnection
        }
        konnection.ipv4Observation() { [self] ipv4 in
            self.ipv4 = ipv4
        }
        konnection.ipv6Observation() { [self] ipv6 in
            self.ipv6 = ipv6
        }
    }
}

extension Color {
    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}

extension Optional where Wrapped == NetworkConnection {
    var icon: String {
        get {
            guard let unwrapped = self else {
                return "NoneConnection"
            }
            switch unwrapped {
                case .wifi: return "WifiConnection"
                case .mobile: return "MobileConnection"
                default: return "UnKnown"
            }
        }
    }

    var description: String {
        get {
            guard let unwrapped = self else {
                return "No Connection"
            }
            switch unwrapped {
                case .wifi: return "Connected By Wify"
                case .mobile: return "Connected By Mobile Network"
                default: return "UnKnown"
            }
        }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
