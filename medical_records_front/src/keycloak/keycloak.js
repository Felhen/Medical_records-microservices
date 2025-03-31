import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8180/',
  realm: 'medical-records',
  clientId: 'medical_app', 
});

export default keycloak;