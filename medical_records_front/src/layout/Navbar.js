import React from 'react';
import { useAuth } from '../context/AuthProvider';


export default function Navbar() {

  const { login, isLoggedIn, errorMessage, userRole, logout } = useAuth();


  const toggleDropdown = () => {
    const dropdownMenu = document.querySelector('.dropdown-menu');
    if (dropdownMenu) {
      dropdownMenu.classList.toggle('show');
    }
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          <div className="dropdown" onClick={toggleDropdown}>
            <button className="btn btn-secondary dropdown-toggle" type="button" aria-expanded="false">
              Dropdown button
            </button>
            <ul className="dropdown-menu">
               {isLoggedIn ? (
                <li><a className="dropdown-item" onClick={()=> {if(isLoggedIn) {logout();}}}>Logout</a></li>
               ) :
               <>
               <li><a className="dropdown-item" href="/login">Login</a></li>
               <li><a className="dropdown-item" href="/register">Register</a></li>
               </>
               }
            </ul>
          </div>
        </div>
      </nav>
    </div>
  );
}

