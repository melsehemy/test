import React, {useState, useEffect} from 'react';
import {View, StyleSheet, Alert, NativeModules, Text} from 'react-native';
// import {NeotekOB, changeLanguage} from 'neotek-mobile-ob-sdk';
import AsyncStorage from '@react-native-async-storage/async-storage';
if (__DEV__) {
  require('../ReactotronConfig');
}
export default function App() {
  const [lang, setLang] = useState<'en' | 'ar'>('en');
  // const handleLanguageChange = async (newLang: 'en' | 'ar') => {
  //   await AsyncStorage.setItem('language', newLang);
  //   setLang(newLang);
  //   await changeLanguage(newLang);
  // };
  NativeModules.InsecureApi.fetchUrl(
    'https://gateway-am-wso2-nonprod.apps.nt-non-ocp.neotek.sa/ecr/v1/auth/user',
  )
    .then(response => {
      console.log(response);
      Alert.alert('response', JSON.stringify(response));
    })
    .catch(error => {
      console.error(error);
      Alert.alert('s', JSON.stringify(error));
    });
  const Theme = {
    colors: {
      primary: '#0CBAB4',
      secondary: '#D8ECEB',
      positive: '#00e676',
      negative: '#d32f2f',
      rejected: '#f44336',
      expired: '#767676FF',
      pending: '#ff9800',
      text: '#000000',
      heading: '#333333',
      subheading: '#666666',
      background: '#ffffff',
      card: '#f5f5f5',
    },
  };
  return (
    <>
      <Text>GG</Text>
      {/* <NeotekOB
        mode="light"
        theme={Theme}
        psuId="255cc"
        clientId="d905b5912cdc6c066b6672eb946f18f6"
        clientSecret="c40271af19ce9a7124c061d45283ed34"
        products={[
          'ob_connect',
          'iban_verification',
          'income_verification',
          'single_api',
          'e_statements',
        ]}
      /> */}
    </>
  );
}
