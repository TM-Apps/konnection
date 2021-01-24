//
//  ContentView.swift
//  Sample
//
//  Created by TMApps on 29/12/2020.
//

import SwiftUI
import Konnection

struct ContentView: View {
    @State var connection: NetworkConnection = .none

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
            }
        }
        .onAppear { onViewAppear() }
    }

    private func onViewAppear() {
        konnection.networkConnectionObservation() { [self] networkConnection in
            self.connection = networkConnection
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

extension NetworkConnection {
    var icon: String {
        get {
            switch self {
                case .wifi: return "WifiConnection"
                case .mobile: return "MobileConnection"
                case .none: return "NoneConnection"
                default: return "UnKnown"
            }
        }
    }

    public override var description: String {
        get {
            switch self {
                case .wifi: return "Connected By Wify"
                case .mobile: return "Connected By Mobile Network"
                case .none: return "No Connection"
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
