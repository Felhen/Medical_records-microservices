import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import SearchBar from "../entities/SearchBar";

export default function DoctorDashboard() {
    const [patients, setPatients] = useState([]);

    useEffect(() => {
        loadPatients();
    }, []);

    const loadPatients = async () => {
        const result = await axios.get("http://localhost:8080/patients");
        setPatients(result.data);
    };

    return (
        <div className="container mt-4">
            {/* Header Section */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h1 className="text-primary">Doctor Dashboard</h1>
                <SearchBar />
            </div>

            {/* Patients Table */}
            <div className="card shadow">
                <div className="card-header bg-primary text-white">
                    <h5 className="mb-0">All Patients</h5>
                </div>
                <div className="card-body p-0">
                    <table className="table table-hover mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>#</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Birthdate</th>
                                <th>Person Number</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {patients.length > 0 ? (
                                patients.map((patient, index) => (
                                    <tr key={patient.id}>
                                        <td>{index + 1}</td>
                                        <td>{patient.first_name}</td>
                                        <td>{patient.last_name}</td>
                                        <td>{patient.birth_date}</td>
                                        <td>{patient.person_number}</td>
                                        <td>
                                            <div className="btn-group">
                                                <Link to={`/patientinfo/${patient.id}`} className="btn btn-sm btn-outline-primary">View</Link>
                                                <Link to={`/add_encounter/${patient.id}`} className="btn btn-sm btn-outline-success">Encounter</Link>
                                                <Link to={`/add_observation/${patient.id}`} className="btn btn-sm btn-outline-warning">Observation</Link>
                                                <Link to={`/add_condition/${patient.id}`} className="btn btn-sm btn-outline-danger">Condition</Link>
                                                <Link to={`/images/${patient.id}`} className="btn btn-sm btn-outline-secondary">Images</Link>
                                            </div>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="6" className="text-center text-muted">No patients found.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
