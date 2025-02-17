import React, { useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useAuth } from "../context/AuthProvider";

const AddEncounter = () => {
    const { patientId } = useParams();
    const { userId } = useAuth();
    const doctorId = userId;
    
    const [date, setDate] = useState('');
    const [description, setDescription] = useState('');

    const handleDescriptionChange = (e) => {
        setDescription(e.target.value);
    };

    const handleEncounterAddition = async () => {
        try {
            const encounterDetails = {
                date,
                description,
                doctorId,
            };
            console.log(doctorId)
            const response = await axios.post(`http://localhost:8081/${patientId}/add_encounter`, encounterDetails);

            console.log(response.data);
            
        } catch (error) {
            // Handle encounter addition error
            console.error('Encounter addition failed:', error);
        }
    };

    return (
        <div className="container">
            <h2>Add Encounter</h2>
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
            <button className="btn btn-primary" onClick={handleEncounterAddition}>
                Add Encounter
            </button>
        </div>
    );
};

export default AddEncounter;
