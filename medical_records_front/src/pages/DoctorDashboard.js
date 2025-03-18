// DoctorDashboard.js
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import SearchBar from "../entities/SearchBar";

export default function DoctorDashboard() {

    const [patients, setPatients] = useState([]);

    useEffect(()=>{
        loadPatients();
    },[]);

    const loadPatients=async()=>{
        const result = await axios.get("http://localhost:8080/patients")
        setPatients(result.data);
    };

  return (
    <div className='container'>
        <div className='py-4'>
        <div>
      <h1>Doctor Dashboard</h1>
      <SearchBar/>
    </div>
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">First name</th>
                        <th scope="col">Last name</th>
                        <th scope="col">Birthdate</th>
                        <th scope="col">Person number</th>
                        <th scope="col">More patient information</th>
                        <th scope="col">Add encounter</th>
                        <th scope="col">Add observation</th>
                        <th scope="col">Add condition</th>
                        <th scope="col">Images</th> 
                    </tr>
                </thead>
                <tbody>
                    {
                        patients.map((patient, index)=>(
                            <tr>
                                <th scope="row" key={index}>{index+1}</th>
                                <td>{patient.first_name}</td>
                                <td>{patient.last_name}</td>
                                <td>{patient.birth_date}</td>
                                <td>{patient.person_number}</td>
                                <td>
                                    <Link to={`/patientinfo/${patient.id}`} className='btn btn-primary mx-2'>View</Link>
                                </td>
                                <td>
                                    <Link to={`/add_encounter/${patient.id}`} className='btn btn-primary mx-2'>Add</Link>
                                </td>
                                <td>
                                    <Link to={`/add_observation/${patient.id}`} className='btn btn-primary mx-2'>Add</Link>
                                </td>
                                <td>
                                    <Link to={`/add_condition/${patient.id}`} className='btn btn-primary mx-2'>Add</Link>
                                </td>
                                <td>
                                    <Link to={`/images/${patient.id}`} className="btn btn-secondary mx-2">Images</Link>
                                </td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        </div>
    </div>
  )
}
