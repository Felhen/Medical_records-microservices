// src/utils/securedAxios.js
import axios from 'axios';
import keycloak from '../keycloak/keycloak';

const securedAxios = (baseURL) => {
  const instance = axios.create({
    baseURL: baseURL,
  });

  instance.interceptors.request.use(async (config) => {
    await keycloak.updateToken(5);
    config.headers.Authorization = `Bearer ${keycloak.token}`;
    return config;
  });

  return instance;
};

export default securedAxios;
