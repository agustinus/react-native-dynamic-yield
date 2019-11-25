
# react-native-dynamic-yield
## Getting started for RN 0.60+
Use version >= v2.0.0
This module hasn't been published to npm registry, so please fork this to your own repository or use mine to install it into your project.
`$ npm install <git remote url> --save`

Autolink will be done since RN 0.60. Please don't forget to do `pod install`
Use `$ react-native unlink react-native-dynamic-yield` to unlink existing linked package.

## Getting started for RN < 0.60
Use version < v2.0.0
This module hasn't been published to npm registry, so please fork this to your own repository or use mine to install it into your project.
`$ npm install <git remote url> --save`

### Mostly automatic installation

`$ react-native link react-native-dynamic-yield`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-dynamic-yield` and add `RNDynamicYield.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNDynamicYield.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNDynamicYieldPackage;` to the imports at the top of the file
  - Add `new RNDynamicYieldPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-dynamic-yield'
  	project(':react-native-dynamic-yield').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-dynamic-yield/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-dynamic-yield')
  	```

## Usage
```javascript
import RNDynamicYield from 'react-native-dynamic-yield';

// TODO: What to do with the module?
RNDynamicYield;
```
  
