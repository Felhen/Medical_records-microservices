import React, { useState, useEffect } from "react";
import securedAxios from "../keycloak/SecuredAxios";


const SearchBar = () => {
  const [searchType, setSearchType] = useState("patientsByName"); // Default search type
  const [searchQuery, setSearchQuery] = useState("");
  const [searchDate, setSearchDate] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  useEffect(() => {
    setSearchQuery(""); // Clears input field when search type changes
  }, [searchType]);

  const handleSearch = async () => {
    try {
      let response;
      if (searchType === "patientsByName") {
        response = await securedAxios('8083').get("/search/patients", {
          params: { name: searchQuery.trim() }
        });
      } else if (searchType === "patientsByDoctor") {
        response = await securedAxios('8083').get("/search/patients/by-doctor", {
          params: { doctorName: searchQuery.trim() }
        });
      } else if (searchType === "conditions") {
        response = await securedAxios('8083').get("/search/conditions", {
          params: { conditionName: searchQuery.trim() }
        });
      } else if (searchType === "encountersByDoctorAndDate") {
        response = await securedAxios('8083').get("/search/encounters/by-date", {
          params: { doctorName: searchQuery.trim(), date: searchDate },
        });
      }


      setSearchResults(response.data);
    } catch (error) {
      console.error("Search failed:", error);
      setSearchResults([]);
    }
  };

  return (
    <div>
      <h2>Search</h2>
      <select value={searchType} onChange={(e) => setSearchType(e.target.value)}>
        <option value="patientsByName">Search Patients by Name</option>
        <option value="patientsByDoctor">Search Patients by Doctor</option>
        <option value="conditions">Search Conditions</option>
        <option value="encountersByDoctorAndDate">Search Encounters by Doctor & Date</option>
      </select>

      <input
        type="text"
        placeholder="Enter search query..."
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />

      {searchType === "encountersByDoctorAndDate" && (
        <input
          type="date"
          value={searchDate}
          onChange={(e) => setSearchDate(e.target.value)}
        />
      )}

      <button onClick={handleSearch}>Search</button>

      {/* Display search results */}
      <div>
        {searchResults.length > 0 ? (
          <ul>
            {searchResults.map((result, index) => (
              <li key={index}>
              {searchType === "patientsByName" || searchType === "patientsByDoctor"
                ? `${result.firstName} ${result.lastName} (ID: ${result.patientId})`
                : searchType === "encountersByDoctorAndDate"
                ? `Encounter Date: ${result.encounterDate}, Patient: ${result.patientName}, Info: ${result.encounterInfo}`
                : searchType === "conditions"
                ? `${result.conditionName} - ${result.conditionInfo} (Patient: ${result.firstName} ${result.lastName})`
                : "Unknown search type"}
              </li>
            ))}
          </ul>
        ) : (
          <p>No results found.</p>
        )}
      </div>
    </div>
  );
};

export default SearchBar;
