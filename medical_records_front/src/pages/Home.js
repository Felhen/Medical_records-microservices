import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';

const Home = () => {
  const { userRole } = useAuth();

  // Function to get the correct dashboard link based on userRole
  const getDashboardLink = () => {
    switch (userRole) {
      case 'PATIENT':
        return '/patient';
      case 'DOCTOR':
        return '/doctor';
      case 'STAFF':
        return '/staff';
      default:
        return '/';
    }
  };

  return (
    <div className='container'>
      <div className='py-4'>
        <h1>Hello {userRole}</h1>
        <Link to={getDashboardLink()}>
          <button className='btn btn-primary'>Go to Dashboard</button>
        </Link>
      </div>
    </div>
  );
};

export default Home;
