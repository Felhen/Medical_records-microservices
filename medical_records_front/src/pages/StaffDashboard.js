import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import securedAxios from "../keycloak/SecuredAxios";
import SearchBar from "../entities/SearchBar";

export default function StaffDashboard() {
  const [patients, setPatients] = useState([]);

  useEffect(() => {
    loadPatients();
  }, []);

  const API_BASE = process.env.REACT_APP_USER_API;

  const loadPatients = async () => {
    try {
      const result = await securedAxios(API_BASE).get("/patients");
      setPatients(result.data);
    } catch (error) {
      console.error("Error loading patients:", error);
    }
  };
  

  return (
    <div className="container mt-4">
      {/* Header Section */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="text-primary">Staff Dashboard</h1>
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
                        <Link
                          to={`/add_encounter/${patient.id}`}
                          className="btn btn-sm btn-outline-success"
                        >
                          Encounter
                        </Link>
                        <Link
                          to={`/add_observation/${patient.id}`}
                          className="btn btn-sm btn-outline-warning"
                        >
                          Observation
                        </Link>
                        <Link
                          to={`/add_condition/${patient.id}`}
                          className="btn btn-sm btn-outline-danger"
                        >
                          Condition
                        </Link>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center text-muted">
                    No patients found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
