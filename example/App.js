/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { useState, useEffect } from 'react';
import DynamicYield from 'react-native-dynamic-yield';

import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Platform,
  NativeEventEmitter,
} from 'react-native';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';

// Update these keys and variable as per your dynamic yield setup
const SecretKeyIOS = '<your-own-dy-ios-key>';
const SecretKeyAndroid = '<your-own-dy-android-key>';
const DYVariableName = 'recommendation-home';

const App = () => {
  const [isReady, setDYStatus] = useState(false);
  const [dyVariable, setDYVariable] = useState('-');

  useEffect(() => {
    let status = false;
    const dyExpStatusListener = new NativeEventEmitter(DynamicYield).addListener(DynamicYield.DY_EVENT_EXPERIMENTS_READY,
      (result) => {
        if (result.state === DynamicYield.DY_EXP_STATE_READY) {
          if (!status) {
            status = true;
            setDYStatus(true);
            DynamicYield.getDynamicVariable(DYVariableName, 'Default Variable');
          } 
        } else {
          setDYStatus(false);
        }
      });

    const dyVariableListener = new NativeEventEmitter(DynamicYield).addListener(DynamicYield.DY_EVENT_DYNAMIC_VARIABLE,
      (result) => {
        setDYVariable(result.value);
        console.log("Variable: ", result.value);
      }
    );

    if (Platform.OS === 'ios') {
      DynamicYield.setSecretKey(SecretKeyIOS, true);
    } else {
      DynamicYield.setSecretKey(SecretKeyAndroid, true);
    }
    DynamicYield.enableDeveloperLogs(__DEV__);

    return () => {
      dyExpStatusListener.remove();
      dyVariableListener.remove();
    }
  }, []);

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          {global.HermesInternal == null ? null : (
            <View style={styles.engine}>
              <Text style={styles.footer}>Engine: Hermes</Text>
            </View>
          )}
          <View style={styles.body}>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>DY SDK Status</Text>
              <Text style={styles.sectionDescription}>
                {isReady.toString()}
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>DY Variable</Text>
              <Text style={styles.sectionDescription}>
                {dyVariable.toString()}
              </Text>
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
