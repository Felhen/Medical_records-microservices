import React, { useState } from 'react';
import securedAxios from '../keycloak/SecuredAxios';
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
      
          const API_BASE = process.env.REACT_APP_RECORDS_API;

          const response = await securedAxios(API_BASE).post(`/${patientId}/add_condition`, conditionDetails);

          console.log(response.data);
        } catch (error) {
          console.error("Condition addition failed:", error);
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
