import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'https://felixh-keycloak.cloud.cbh.kth.se/',
  realm: 'medical-records',
  clientId: 'medical_app', 
});

export default keycloak;