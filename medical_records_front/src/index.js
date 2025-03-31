// src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';
import keycloak from './keycloak/keycloak';
import { ReactKeycloakProvider } from '@react-keycloak/web';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <ReactKeycloakProvider 
      authClient={keycloak}
      initOptions={{
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
    }}>
    <React.StrictMode>
      <App />
    </React.StrictMode>
  </ReactKeycloakProvider>
);

