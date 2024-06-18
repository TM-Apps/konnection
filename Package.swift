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
         url: "https://github.com/TM-Apps/konnection/releases/download/1.4.1/Konnection.xcframework.zip",
         checksum: "1112a9fd4f73ad728dc56c977521d2b4ab53e38d848c88a68ff41e9c571a6457"
      )
   ]
)