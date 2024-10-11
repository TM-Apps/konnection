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
         url: "https://github.com/TM-Apps/konnection/releases/download/1.4.3/Konnection.xcframework.zip",
         checksum: "9d5e749564cc2e8d94cf89d9846ba66d03e98ae55b835d820369f3101eeed324"
      )
   ]
)
