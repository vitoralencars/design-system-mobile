// swift-tools-version: 5.9

import PackageDescription

let package = Package(
  name: "DesignSystem",
  platforms: [.iOS(.v15)],
  products: [
    .library(
      name: "DesignSystem",
      targets: ["DesignSystem"]),
  ],
  targets: [
    .binaryTarget(
        name: "DesignSystem",
        path: "./DesignSystemMobile.xcframework"
    ),
  ]
)
