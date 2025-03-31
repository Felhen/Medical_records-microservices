import React, { useState } from 'react';
import securedAxios from '../keycloak/SecuredAxios';
import { useParams } from 'react-router-dom';
import { useAuth } from "../context/AuthProvider";

const AddObservation = () => {
    const { patientId } = useParams();
    const { userId } = useAuth();
    const doctorId = userId;

    const [date, setDate] = useState('');
    const [description, setDescription] = useState('');

    const handleDescriptionChange = (e) => {
        setDescription(e.target.value);
    };

    const handleObservationAddition = async () => {
        try {
          const observationDetails = {
            date,
            description,
            doctorId,
          };
      
          const response = await securedAxios('8081').post(`/${patientId}/add_observation`,observationDetails);
          console.log(response.data)
        } catch (error) {
          console.error("Observation addition failed:", error);
        }
      };
      

    return (
        <div className="container">
            <h2>Add Observation</h2>
            <div className="mb-3">
                <label htmlFor="date">Date</label>
                <input
                    type="date"
                    className="form-control"
                    name="date"
                    onChange={(e) => setDate(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="description">Description</label>
                <textarea
                    className="form-control"
                    name="description"
                    onChange={handleDescriptionChange}
                    value={description}
                ></textarea>
            </div>
            <button className="btn btn-primary" onClick={handleObservationAddition}>
                Add Observation
            </button>
        </div>
    );
};

export default AddObservation;
