import React, { useState } from "react";
import axios from "axios";

const SearchBar = ({ userRole }) => {
  const [searchType, setSearchType] = useState("patientsByName"); // Default search type
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = async () => {
    try {
      let response;
      if (searchType === "patientsByName") {
        response = await axios.get(`http://localhost:8083/search/patients`, {
          params: { firstName: searchQuery.split(" ")[0], lastName: searchQuery.split(" ")[1] || "" }
        });
      } else if (searchType === "patientsByDoctor") {
        response = await axios.get(`http://localhost:8083/search/patients/by-doctor`, {
          params: { doctorId: userRole === "DOCTOR" ? localStorage.getItem("userId") : searchQuery }
        });
      } else if (searchType === "conditions") {
        response = await axios.get(`http://localhost:8083/search/conditions`, {
          params: { conditionName: searchQuery }
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
        {userRole === "STAFF" && <option value="patientsByDoctor">Search Patients by Doctor</option>}
        <option value="conditions">Search Conditions</option>
      </select>
      <input
        type="text"
        placeholder="Enter search query..."
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <button onClick={handleSearch}>Search</button>

      {/* Display search results */}
      <div>
        {searchResults.length > 0 ? (
          <ul>
            {searchResults.map((result, index) => (
              <li key={index}>
                {searchType === "patientsByName" || searchType === "patientsByDoctor"
                  ? `${result.firstName} ${result.lastName} (ID: ${result.patientId})`
                  : `${result.conditionName} - ${result.conditionInfo}`}
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
