'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight,
  ToastAndroid
} from 'react-native';

const superID = require('react-native').NativeModules.SuperIDRN;
superID.debug(true);
superID.registe('EKsrdtS3p67n4hAGtqUx2dpO', 'bgUip4gieeBYoLJnI9beN5XK');

class SimpleApp extends Component {

  constructor(props) {
    super(props);
  
    this.state = {};
    superID.version().then((ret) => {
      this.setState({version: `version: ${ret.version} build: ${ret.build}`});
    }).catch(console.log);
    
  }

  async _login() {
    try {
      const ret = await superID.login();
      if (ret !== null){
        this.setState({info: `User: ${ret.userInfo.name}  Phone: ${ret.userInfo.phone}`});
      }
    } catch (error) {
      console.log(error);
    }
  }

  async _verify() {
    try {
      const ret = await superID.verify(1);
      if (ret !== null){
        const result = ret ? 'Verify Succeed!' : 'Verify Fail !'
        this.setState({info: result});
      }
    } catch (error) {
      this.setState({info: 'Please Login first!'});
    }
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
        <Text style={styles.instructions}>
          {this.state.info}
        </Text>
        <TouchableHighlight
          style={styles.button}
          onPress={this._login.bind(this)}>
          <Text style={styles.buttonText}>Login</Text>
        </TouchableHighlight>

        <TouchableHighlight
          style={styles.button}
          onPress={this._verify.bind(this)}>
          <Text style={styles.buttonText}>Verify</Text>
        </TouchableHighlight>
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
  button: {
    margin: 5,
  },
});

AppRegistry.registerComponent('SimpleApp', () => SimpleApp);
