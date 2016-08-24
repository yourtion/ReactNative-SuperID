# ReactNative-SuperID

SuperID SDK in ReactNative

## Installation

```
npm i --save superid-react-native
```

### Configure your React Native Application

#### Mostly automatic install with react-native

```
react-native link  superid-react-native
```

#### iOS

Cocapods:

```ruby
pod 'SuperIDRN', :path => './node_modules/superid-react-native/iOS'
```

Run `pod install`

#### Android

1. `android/settings.gradle`:: Add the following snippet

```gradle
include ':SuperIDRN'
project(':SuperIDRN').projectDir = file('../node_modules/superid-react-native/Android')
```

2. `android/app/build.gradle`: Add in dependencies block.

```gradle
compile project(':SuperIDRN')
```

3. in your `MainActivity` (or equivalent) the SIDRNPackage needs to be added. Add the import at the top:

```java
import com.projectseptember.SuperIDRN.SIDRNPackage;
```

4. In order for React Native to use the package, add it the packages inside of the class extending ReactActivity.

```java
@Override
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
  new MainReactPackage(),
  ...
  new SIDRNPackage()
  );
}
```
