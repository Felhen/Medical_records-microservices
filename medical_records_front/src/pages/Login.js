import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';

const LoginForm = () => {
  const { login, isLoggedIn, errorMessage, userRole } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); // Use useNavigate hook for navigation

  useEffect(() => {
    if (isLoggedIn) {
            
        if (userRole === 'PATIENT') {
        navigate('/patient');
        } else if (userRole === 'DOCTOR') {
        navigate('/doctor');
        } else if (userRole === 'STAFF') {
        navigate('/staff');
        }
    }
  }, [isLoggedIn]);

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
        await login(username, password);
    } catch (error) {
      // Error handling might be handled in the context and updated in the state
      console.error('Login error:', error);
    }
  };

    return (
        <div className="container">
            <h2 className="text-center mb-3">Login</h2>
            <form className="mx-auto" style={{ maxWidth: '300px' }} onSubmit={handleLogin}>
                <div className="mb-3">
                    <label htmlFor="InputUsername" className="form-label">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        id="InputUsername"
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="InputPassword" className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="InputPassword"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit" className="btn btn-primary mx-2">Login</button>
                <Link to="/register" className="btn btn-primary">Register</Link>
                {errorMessage && <p>{errorMessage}</p>}
            </form>
        </div>
    );
};

export default LoginForm;

