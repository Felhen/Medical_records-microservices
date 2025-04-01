import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import securedAxios from '../keycloak/SecuredAxios';

export default function PatientInfo() {
    const { patientId } = useParams(); // Get the patientId from the URL parameter
    const [patient, setPatient] = useState(null);
    const [observations, setObservations] = useState([]);
    const [encounters, setEncounters] = useState([]);
    const [conditions, setConditions] = useState([]);

    useEffect(() => {
        const fetchPatientInfo = async () => {
            try {
                const response = await securedAxios('8084').get(`/patient/${patientId}`);
                setPatient(response.data);
                const responseCond = await securedAxios('8081').get(`/patient/${patientId}/conditions`);
                setConditions(responseCond.data);
                const responseEnc = await securedAxios('8081').get(`/patient/${patientId}/encounters`);
                setEncounters(responseEnc.data);
                const responseObs = await securedAxios('8081').get(`/patient/${patientId}/observations`);
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
        <div className="container py-5" style={{ maxWidth: '1200px' }}>
          <div className="row">
            <div className="col-12">
      
              {/* Patient Information */}
              {patient ? (
                <div className="card mb-4 shadow-sm">
                  <div className="card-header bg-primary text-white">
                    <h4 className="mb-0">Patient Information</h4>
                  </div>
                  <div className="card-body">
                    <table className="table mb-0">
                      <tbody>
                        <tr>
                          <th style={{ width: '30%' }}>First Name:</th>
                          <td>{patient.first_name}</td>
                        </tr>
                        <tr>
                          <th>Last Name:</th>
                          <td>{patient.last_name}</td>
                        </tr>
                        <tr>
                          <th>Date of Birth:</th>
                          <td>{patient.birth_date}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              ) : (
                <div className="alert alert-info text-center">Loading patient information...</div>
              )}
      
              {/* Observations */}
              <div className="card mb-4 shadow-sm">
                <div className="card-header bg-info text-white">
                  <h5 className="mb-0">Observations</h5>
                </div>
                <div className="card-body p-0">
                  <table className="table mb-0">
                    <thead className="table-light">
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
                </div>
              </div>
      
              {/* Encounters */}
              <div className="card mb-4 shadow-sm">
                <div className="card-header bg-warning text-dark">
                  <h5 className="mb-0">Encounters</h5>
                </div>
                <div className="card-body p-0">
                  <table className="table mb-0">
                    <thead className="table-light">
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
                </div>
              </div>
      
              {/* Conditions */}
              <div className="card mb-4 shadow-sm">
                <div className="card-header bg-danger text-white">
                  <h5 className="mb-0">Conditions</h5>
                </div>
                <div className="card-body p-0">
                  <table className="table mb-0">
                    <thead className="table-light">
                      <tr>
                        <th>Name</th>
                        <th>Date</th>
                        <th>Info</th>
                      </tr>
                    </thead>
                    <tbody>
                      {conditions.map((condition, index) => (
                        <tr key={index}>
                          <td>{condition.condition_date}</td>
                          <td>{condition.condition_name}</td>
                          <td>{condition.condition_info}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
      
            </div>
          </div>
        </div>
      );
      
}
