import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://host.docker.internal:8080/',
  realm: 'medical-records',
  clientId: 'medical_app', 
});

export default keycloak;