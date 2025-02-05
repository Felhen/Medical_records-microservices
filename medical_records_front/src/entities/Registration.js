import React, { useState } from 'react';
import axios from 'axios';

const Registration = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthdate, setBirthdate] = useState('');
    const [personNumber, setPersonNumber] = useState('');
    const [role, setRole] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name === 'role') {
            // Reset form fields when role changes
            setUsername('');
            setPassword('');
            setFirstName('');
            setLastName('');
            setBirthdate('');
            setPersonNumber('');
        }
        setRole(value);
    };

    const handleRegistration = async () => {
        try {
            let userData = { username, password, role };
    
            if (role === 'patient') {
                userData = {
                    ...userData,
                    firstName,
                    lastName,
                    birthdate,
                    personNumber,
                };
            }
    
            const response = await axios.post('http://localhost:8080/register', userData);
    
            console.log(response.data);
            // Redirect or perform any necessary actions after successful registration
            // ...
        } catch (error) {
            // Handle registration error
            console.error('Registration failed:', error);
        }
    };
    

    return (
        <div className="container">
            <h2>Registration</h2>
            <div className="mb-3">
                <label htmlFor="role">Select Role:</label>
                <select className="form-select" name="role" onChange={handleInputChange} value={role}>
                    <option value="">Select Role</option>
                    <option value="doctor">Doctor</option>
                    <option value="staff">Staff</option>
                    <option value="patient">Patient</option>
                </select>
            </div>

            {(role === 'patient') && (
                <div>
                    <div className="mb-3">
                        <label htmlFor="firstName">First Name</label>
                        <input type="text" className="form-control" name="firstName" onChange={(e) => setFirstName(e.target.value)} />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="lastName">Last Name</label>
                        <input type="text" className="form-control" name="lastName" onChange={(e) => setLastName(e.target.value)} />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="birthdate">Birthdate</label>
                        <input
                            type="date"
                            className="form-control"
                            name="birthdate"
                            onChange={(e) => setBirthdate(e.target.value)}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="personNumber">Person Number</label>
                        <input type="text" className="form-control" name="personNumber" onChange={(e) => setPersonNumber(e.target.value)} />
                    </div>
                </div>
            )}

            <div className="mb-3">
                <label htmlFor="username">Username</label>
                <input type="text" className="form-control" name="username" onChange={(e) => setUsername(e.target.value)} />
            </div>

            <div className="mb-3">
                <label htmlFor="password">Password</label>
                <input type="password" className="form-control" name="password" onChange={(e) => setPassword(e.target.value)} />
            </div>

            <button className="btn btn-primary" onClick={handleRegistration}>
                Register
            </button>
        </div>
    );
};

export default Registration;

