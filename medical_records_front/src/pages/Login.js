import React, { useEffect } from 'react';
import { useAuth } from '../context/AuthProvider';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const { isLoggedIn, userRole, login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isLoggedIn) {
      if (userRole === 'PATIENT') navigate('/patient');
      else if (userRole === 'DOCTOR') navigate('/doctor');
      else if (userRole === 'STAFF') navigate('/staff');
    }
  }, [isLoggedIn, userRole, navigate]);

  const handleRegister = () => {
    navigate('/register');
  };

  return (
    <div className="container text-center mt-5">
      <h1 className="mb-3 text-primary">Welcome to the Medical Records Portal</h1>
      <p className="mb-4 text-muted">Secure access for patients, doctors, and staff</p>
      <div className="d-flex justify-content-center gap-3">
        <button className="btn btn-primary btn-lg" onClick={login}>
          Login with Keycloak
        </button>
        <button className="btn btn-success btn-lg" onClick={handleRegister}>
          Register
        </button>
      </div>
    </div>
  );
};

export default Login;
