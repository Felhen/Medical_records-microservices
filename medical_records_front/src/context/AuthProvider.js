import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

// Create a context to hold authentication state and functions
const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {

    const initialLoggedInState = localStorage.getItem('isLoggedIn') === 'true';
    const initialUserRole = localStorage.getItem('userRole') || '';
    const initialUserId = localStorage.getItem('userId') || '';
    const initialPatientId = localStorage.getItem('patientId') || '';
  
    const [isLoggedIn, setIsLoggedIn] = useState(initialLoggedInState);
    const [userRole, setUserRole] = useState(initialUserRole);
    const [userId, setUserId] = useState(initialUserId); 
    const [patientId, setPatientId] = useState(initialPatientId);
    const [errorMessage, setErrorMessage] = useState('');
  
    const login = async (username, password) => {
      try {
        const response = await axios.post('http://localhost:8080/login', {
          username: username,
          pass: password,
        });
  
        console.log(response.data);
        if (response.status === 200) {
          const userIdFromResponse = response.data.match(/User ID: (\d+)/)[1]; // Extracting user ID
          const role = response.data.match(/Role: ([^\s]+)/)[1]; // Extracting user role
          const patientIdFromResponse = response.data.match(/Patient ID: (\d+)/)[1]; // Extracting patient ID
          setPatientId(patientIdFromResponse); // Set patient ID in stat
          setUserId(userIdFromResponse); // Set user ID in state
          setUserRole(role);
          setIsLoggedIn(true);
          setErrorMessage('');
  
          // Update localStorage upon successful login
          localStorage.setItem('isLoggedIn', 'true');
          localStorage.setItem('userRole', role);
          localStorage.setItem('userId', userIdFromResponse); 
          localStorage.setItem('patientId', patientIdFromResponse);
        }
      } catch (error) {
        setIsLoggedIn(false);
        setUserRole('');
        setErrorMessage('Invalid username or password');
      }
    };
  
    const logout = () => {
      setIsLoggedIn(false);
      setUserRole('');
      setUserId(''); 
      setPatientId('');
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('userRole');
      localStorage.removeItem('userId'); // Remove user ID from local storage on logout
      localStorage.removeItem('patientId'); // Remove patient ID from local storage on logout
    };
  
    useEffect(() => {
      localStorage.setItem('isLoggedIn', isLoggedIn);
      localStorage.setItem('userRole', userRole);
      localStorage.setItem('userId', userId); // Update user ID in local storage
    }, [isLoggedIn, userRole, userId]);
  
    const authContextValue = {
      isLoggedIn,
      userRole,
      userId,
      patientId,
      errorMessage,
      login,
      logout,
    };
  
    return (
      <AuthContext.Provider value={authContextValue}>
        {children}
      </AuthContext.Provider>
    );
  };
  