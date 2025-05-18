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
         url: "https://github.com/TM-Apps/konnection/releases/download/1.4.4/Konnection.xcframework.zip",
         checksum: "ce8a3566162ea09ad3464426877ca7203c8656dac37da8d2ae6840c357eda845"
      )
   ]
)
