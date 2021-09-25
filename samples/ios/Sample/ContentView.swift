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
    @State var ipInfo: IpInfo? = nil

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

                Text(ipInfo.ipInfo1)
                    .foregroundColor(.white)
                    .font(.title3)
                    .bold()
                    .padding([.bottom, .horizontal])

                Text(ipInfo.ipInfo2)
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

            konnection.getCurrentIpInfo() { [self] result, error in
                self.ipInfo = result
            }
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
                return "NoConnection"
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

extension Optional where Wrapped == IpInfo {
    var ipInfo1: String {
        get {
            if let wifiIpInfo = self as? IpInfo.WifiIpInfo {
                guard let ipv4 = wifiIpInfo.ipv4 else {
                    return ""
                }
                return "IPv4: \(ipv4)"
            }
            if let mobileIpInfo = self as? IpInfo.MobileIpInfo {
                guard let ipv4 = mobileIpInfo.hostIpv4 else {
                    return ""
                }
                return "Host IP: \(ipv4)"
            }
            return ""
        }
    }

    var ipInfo2: String {
        get {
            if let wifiIpInfo = self as? IpInfo.WifiIpInfo {
                guard let ipv6 = wifiIpInfo.ipv6 else {
                    return ""
                }
                return "IPv6: \(ipv6)"
            }
            if let mobileIpInfo = self as? IpInfo.MobileIpInfo {
                guard let externalIpv4 = mobileIpInfo.externalIpV4 else {
                    return ""
                }
                return "External IP: \(externalIpv4)"
            }
            return ""
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
