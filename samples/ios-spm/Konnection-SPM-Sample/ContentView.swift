//
//  ContentView.swift
//  Konnection-SPM-Sample
//
//  Created by Magnum Rocha on 17/06/2024.
//

import SwiftUI
import Konnection

struct ContentView: View {
    @State private var networkConnection: NetworkConnection? = nil
    @State private var ipInfo: ConnectionInfo? = nil

    var body: some View {
        VStack {
            ConnectionTypeView(type: networkConnection)
            
            if let ipv4Info = ipInfo?.ipV4Info {
                Text(ipv4Info)
                    .padding(.top)
            }
            
            if let ipv6Info = ipInfo?.ipV6Info {
                Text(ipv6Info)
                    .padding(.top)
            }

            if let externalIpV4 = ipInfo?.externalIpV4Info {
                Text(externalIpV4)
                    .padding(.top)
            }
        }
        .padding()
        .onChange(of: networkConnection) {
            Task {
                do {
                    ipInfo = try await Konnection.shared.getInfo()
                } catch {
                    print("Can't get the ipInfo: \(error)")
                }
            }
        }
        .onAppear {
            Konnection.shared.observeNetworkConnection().collect { connection in
                networkConnection = connection
            } onCompletion: { throwable in
                print("Complete : \(throwable?.description() ?? "<Success>")")
            }
        }
    }
}

struct ConnectionTypeView: View {
    var type: NetworkConnection? = nil

    var body: some View {
        VStack {
            Image(systemName: type.sfSymbolName)
                .resizable()
                .scaledToFit()
                .frame(width: 150, height: 150)
                .foregroundStyle(.tint)
            
            Text(type.message)
                .padding(.all)
        }
    }
}

extension NetworkConnection? {
    var sfSymbolName: String {
        get {
            switch self {
            case .wifi: return "wifi"
            case .mobile: return "cellularbars"
            case .ethernet: return "app.connected.to.app.below.fill"
            case .bluetoothTethering: return "b.circle"
            case .unknownConnectionType: return "exclamationmark.icloud"
            default: return "icloud.slash"
            }
        }
    }

    var message: String {
        get {
            switch self {
            case .wifi: return "Connected by Wifi"
            case .mobile: return "Connected by Mobile Network"
            case .ethernet: return "Connected by Ethernet"
            case .bluetoothTethering: return "Connected by Bluetooth TETHERING"
            case .unknownConnectionType: return "Connected by Unknown Network"
            default: return "No Connection"
            }
        }
    }
}

extension ConnectionInfo {
    var ipV4Info: String? {
        get {
            if let ip = ipv4 {
                return  "IPv4: \(ip)"
            }
            return nil
        }
    }

    var ipV6Info: String? {
        get {
            if let ip = ipv6 {
                return  "IPv6: \(ip)"
            }
            return nil
        }
    }

    var externalIpV4Info: String? {
        get {
            if let ip = externalIpV4 {
                return "External IP: \(ip)"
            }
            return nil
        }
    }
}

#Preview {
    ConnectionTypeView(type: .wifi)
}
