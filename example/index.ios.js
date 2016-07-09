'use strict';

import React, {Component} from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
} from 'react-native';

import SuperIDView from './SuperIDView';

class SimpleApp extends Component {

  render() {
    return (
      <View style={styles.container}>

        <SuperIDView />

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
});

AppRegistry.registerComponent('SimpleApp', () => SimpleApp);
