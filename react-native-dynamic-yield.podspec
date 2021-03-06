require 'json'
package = JSON.parse(File.read("package.json"))

Pod::Spec.new do |s|
  s.name         = package["name"]
  s.version      = package["version"]
  s.summary      = package["summary"]
  s.description  = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.author       = package["author"]
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/agustinus/react-native-dynamic-yield.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency "React"
  s.dependency "Dynamic-Yield-iOS-SDK"

end
