
Pod::Spec.new do |s|
  s.name         = "RNDynamicYield"
  s.version      = "1.0.0"
  s.summary      = "RNDynamicYield"
  s.description  = <<-DESC
                  RNDynamicYield
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "agustinus.k@gmail.com" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/agustinus/RNDynamicYield.git", :tag => "master" }
  s.source_files  = "RNDynamicYield/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  