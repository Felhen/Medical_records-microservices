// src/context/AuthProvider.js
import React, { createContext, useContext, useEffect, useState } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import securedAxios from '../keycloak/SecuredAxios';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const { keycloak, initialized } = useKeycloak();

  const [userRole, setUserRole] = useState('');
  const [userId, setUserId] = useState('');
  const [patientId, setPatientId] = useState('');

  const isLoggedIn = keycloak?.authenticated;

  useEffect(() => {
    const fetchBackendUser = async (keycloakId) => {
      try {
        const API_BASE = process.env.REACT_APP_USER_API || 'http://localhost:8084';
        const response = await securedAxios(API_BASE).get(`/user/by-keycloak-id/${keycloakId}`);

        const data = response.data;
  
        setUserId(data.userId);
        setUserRole(data.role);
        if (data.patientId) setPatientId(data.patientId);
      } catch (error) {
        console.error("Failed to fetch user from backend:", error);
      }
    };
  
    if (initialized && isLoggedIn && keycloak.tokenParsed) {
      const keycloakId = keycloak.tokenParsed.sub;
      fetchBackendUser(keycloakId);
    }
  }, [initialized, isLoggedIn, keycloak]);
  

  const login = () => keycloak?.login();
  const logout = () => keycloak?.logout({ redirectUri: window.location.origin });

  return (
    <AuthContext.Provider value={{
      isLoggedIn,
      userRole,
      userId,
      patientId,
      login,
      logout,
      initialized
    }}>
      {children}
    </AuthContext.Provider>
  );
};
