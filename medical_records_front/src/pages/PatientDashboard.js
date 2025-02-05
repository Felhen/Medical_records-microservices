// PatientDashboard.js

import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthProvider';
import axios from 'axios';

export default function PatientInfo() {
    const { patientId } = useAuth();
    const [patient, setPatient] = useState(null);
    const [observations, setObservations] = useState([]);
    const [encounters, setEncounters] = useState([]);
    const [conditions, setConditions] = useState([]);

    useEffect(() => {
        const fetchPatientInfo = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/patient/${patientId}`);
                setPatient(response.data);
                const responseCond = await axios.get(`http://localhost:8081/patient/${patientId}/conditions`)
                setConditions(responseCond.data);
                const responseEnc = await axios.get(`http://localhost:8081/patient/${patientId}/encounters`)
                setEncounters(responseEnc.data);
                const responseObs = await axios.get(`http://localhost:8081/patient/${patientId}/observations`)
                setObservations(responseObs.data);
            } catch (error) {
                // Handle error fetching patient information
                console.error('Error fetching patient information:', error);
            }
        };

        if (patientId) {
            fetchPatientInfo(); // Fetch patient information when the component mounts
        }
    }, [patientId]);

    return (
        <div className='patInfo'>
            <div className='py-4'>
                {/* Display Patient Information */}
                {patient ? (
                    <div>
                        <h2>Patient Information</h2>
                        <p>First name: {patient.first_name}</p>
                        <p>Last name: {patient.last_name}</p>
                        <p>Date of Birth: {patient.birth_date}</p>
                    </div>
                ) : (
                    <p>Loading patient information...</p>
                )}
    
                {/* Display Observations */}
                <h2>Observations</h2>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Info</th>
                        </tr>
                    </thead>
                    <tbody>
                        {observations.map((observation, index) => (
                            <tr key={index}>
                                <td>{observation.observation_date}</td>
                                <td>{observation.observation_info}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
    
                {/* Display Encounters */}
                <h2>Encounters</h2>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Info</th>
                        </tr>
                    </thead>
                    <tbody>
                        {encounters.map((encounter, index) => (
                            <tr key={index}>
                                <td>{encounter.encounter_date}</td>
                                <td>{encounter.encounter_info}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
    
                {/* Display conditions */}
                <h2>Conditions</h2>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Date</th>
                            <th>Info</th>
                            
                        </tr>
                    </thead>
                    <tbody>
                        {conditions.map((condition, index) => (
                            <tr key={index}>
                                <td>{condition.condition_name}</td>
                                <td>{condition.condition_date}</td>
                                <td>{condition.condition_info}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
    
}

