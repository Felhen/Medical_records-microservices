import React, { useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useAuth } from "../context/AuthProvider";

const AddCondition = () => {
    const { patientId } = useParams();
    const { userId } = useAuth();
    const doctorId = userId;
    const [date, setDate] = useState('');
    const [description, setDescription] = useState('');
    const [name, setName] = useState('');

    const handleNameChange = (e) => {
        setName(e.target.value);
    };

    const handleDescriptionChange = (e) => {
        setDescription(e.target.value);
    };

    const handleConditionAddition = async () => {
        try {
            const conditionDetails = {
                date,
                description,
                name,
                doctorId,
            };

            const response = await axios.post(`http://localhost:8081/${patientId}/add_condition`, conditionDetails);

            console.log(response.data);
            // Redirect or perform any necessary actions after successful condition addition
            // ...
        } catch (error) {
            // Handle condition addition error
            console.error('Condition addition failed:', error);
        }
    };

    return (
        <div className="container">
            <h2>Add Condition</h2>
            <div className="mb-3">
                <label htmlFor="name">Name</label>
                <input
                    type="text"
                    className="form-control"
                    name="name"
                    onChange={handleNameChange}
                    value={name}
                />
            </div>
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
            <button className="btn btn-primary" onClick={handleConditionAddition}>
                Add Condition
            </button>
        </div>
    );
};

export default AddCondition;
