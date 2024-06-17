// swift-tools-version:5.3
import PackageDescription

let package = Package(
   name: "Konnection",
   platforms: [ .iOS(.v10) ],
   products: [
      .library(name: "Konnection", targets: ["Konnection"])
   ],
   targets: [
      .binaryTarget(
         name: "Konnection",
         url: "https://github.com/TM-Apps/konnection/releases/download/1.4.0/Konnection.xcframework.zip",
         checksum:"764753a5955b0c2bdaaaee4da5e8c932b02e32a3b5dccd62bfd3652689cf4baf"
      )
   ]
)