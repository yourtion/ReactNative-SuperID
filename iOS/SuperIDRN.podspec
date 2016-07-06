Pod::Spec.new do |s|

  s.name         = 'SuperIDRN'
  s.summary      = 'SuperID SDK for ReactNative'
  s.author       = { 'Yourtion' => 'yourtion@gmail.com' }
  s.license      = 'MIT'
  s.version      = '0.1.0'
  s.homepage     = 'https://github.com/yourtion/ReactNative-SuperID'
  s.platform     = :ios, '8.0'
  s.source       = { :git => 'https://github.com/yourtion/ReactNative-SuperID.git' }
  s.source_files = 'SuperIDRN/*.{h,m}'
  s.public_header_files = 'SuperIDRN/SuperID-SDK-iOS/*.h',
  s.vendored_libraries = 'SuperIDRN/SuperID-SDK-iOS/libSuperIDSDK.a'
  s.frameworks = ['Foundation', 'AVFoundation', 'CoreMedia', 'CoreTelephony']
  s.libraries = ['c++', 'SuperIDSDK']
  s.xcconfig = { 'OTHER_LDFLAGS': '$(inherited) -ObjC', 'ENABLE_BITCODE': 'NO' }
  s.resources = 'SuperIDRN/SuperID-SDK-iOS/SuperIDSDKSettings.bundle'

end