'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

const SuperID = require('react-native').NativeModules.SuperIDRN;
SuperID.setDebugMode(true);
SuperID.registerApp('EKsrdtS3p67n4hAGtqUx2dpO', 'bgUip4gieeBYoLJnI9beN5XK');

class SimpleApp extends Component {

  constructor(props) {
    super(props);
  
    this.state = {};
    SuperID.getVersion().then((ret) => {
      this.setState({version: `version: ${ret.version} build: ${ret.build}`});
    }).catch(console.log);
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to SuperID React Native!
        </Text>
        <Text style={styles.instructions}>
          {this.state.version}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('SimpleApp', () => SimpleApp);
