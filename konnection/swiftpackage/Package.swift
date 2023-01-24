// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Konnection",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "Konnection",
            targets: ["Konnection"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "Konnection",
            path: "../build/XCFrameworks/release/Konnection.xcframework"
        ),
    ]
)
