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

                const responseCond = await axios.get(`http://localhost:8081/patient/${patientId}/conditions`);
                setConditions(responseCond.data);

                const responseEnc = await axios.get(`http://localhost:8081/patient/${patientId}/encounters`);
                setEncounters(responseEnc.data);

                const responseObs = await axios.get(`http://localhost:8081/patient/${patientId}/observations`);
                setObservations(responseObs.data);
            } catch (error) {
                console.error('Error fetching patient information:', error);
            }
        };

        if (patientId) fetchPatientInfo();
    }, [patientId]);

    return (
        <div className="container my-4">
            {/* Patient Information */}
            <div className="card shadow-lg p-4 mb-4">
                <h2 className="text-center">Patient Information</h2>
                {patient ? (
                    <div className="card-body">
                        <p><strong>First Name:</strong> {patient.first_name}</p>
                        <p><strong>Last Name:</strong> {patient.last_name}</p>
                        <p><strong>Date of Birth:</strong> {patient.birth_date}</p>
                    </div>
                ) : (
                    <p className="text-center">Loading patient information...</p>
                )}
            </div>

            {/* Observations */}
            <div className="card shadow-lg p-4 mb-4">
                <h2 className="text-center">Observations</h2>
                {observations.length > 0 ? (
                    <table className="table table-striped">
                        <thead className="table-dark">
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
                ) : <p className="text-center">No observations found.</p>}
            </div>

            {/* Encounters */}
            <div className="card shadow-lg p-4 mb-4">
                <h2 className="text-center">Encounters</h2>
                {encounters.length > 0 ? (
                    <table className="table table-striped">
                        <thead className="table-dark">
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
                ) : <p className="text-center">No encounters found.</p>}
            </div>

            {/* Conditions */}
            <div className="card shadow-lg p-4">
                <h2 className="text-center">Conditions</h2>
                {conditions.length > 0 ? (
                    <table className="table table-striped">
                        <thead className="table-dark">
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
                ) : <p className="text-center">No conditions found.</p>}
            </div>
        </div>
    );
}
