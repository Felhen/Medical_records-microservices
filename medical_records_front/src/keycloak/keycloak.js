import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'https://felixhr-keycloak.cloud.cbh.kth.se/realms/medical-records',
  realm: 'medical-records',
  clientId: 'medical_app', 
});

export default keycloak;