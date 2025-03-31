import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Navbar from './layout/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Registration from './entities/Registration';
import { AuthProvider, useAuth } from './context/AuthProvider';
import DoctorDashboard from './pages/DoctorDashboard';
import PatientDashboard from './pages/PatientDashboard';
import StaffDashboard from './pages/StaffDashboard';
import "./App.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import PatientInfo from './pages/PatientInfo';
import AddEncounter from './entities/AddEncounter';
import AddObservation from './entities/AddObservation';
import AddCondition from './entities/AddCondition';
import MessagePage from './pages/MessagePage';
import AddMessage from './entities/AddMessage';
import ImagesPage from "./pages/ImagesPage";




const ProtectedRoute = ({ children, allowedRoles }) => {
  const { isLoggedIn, userRole, initialized } = useAuth();

  if (!initialized) return <div>Loading authentication...</div>;

  if (!isLoggedIn) return <Navigate to="/login" />;

  if (!allowedRoles.includes(userRole)) {
    if (userRole === 'PATIENT') return <Navigate to="/patient" />;
    if (userRole === 'DOCTOR') return <Navigate to="/doctor" />;
    if (userRole === 'STAFF') return <Navigate to="/staff" />;
  }
  return children;
};


function App() {
  return (
    <Router>
    <AuthProvider>
      <div className="App">
          <Navbar />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Registration />} />
            <Route path="/" element={<ProtectedRoute allowedRoles={['PATIENT', 'DOCTOR', 'STAFF']}>
              <Home />
            </ProtectedRoute>} />
            <Route path="/patient" element={<ProtectedRoute allowedRoles={['PATIENT']}>
              <PatientDashboard />
            </ProtectedRoute>} />
            <Route path="/doctor" element={<ProtectedRoute allowedRoles={['DOCTOR']}>
              <DoctorDashboard />
            </ProtectedRoute>} />
            <Route path="/staff" element={<ProtectedRoute allowedRoles={['STAFF']}>
              <StaffDashboard />
            </ProtectedRoute>} />
            <Route path="/patientinfo/:patientId" element={<ProtectedRoute allowedRoles={['DOCTOR']}>
              <PatientInfo />
            </ProtectedRoute>} />
            <Route path="/add_encounter/:patientId" element={<ProtectedRoute allowedRoles={['DOCTOR', 'STAFF']}>
              <AddEncounter />
            </ProtectedRoute>} />
            <Route path="/add_observation/:patientId" element={<ProtectedRoute allowedRoles={['DOCTOR', 'STAFF']}>
              <AddObservation/>
            </ProtectedRoute>} />
            <Route path="/add_condition/:patientId" element={<ProtectedRoute allowedRoles={['DOCTOR', 'STAFF']}>
              <AddCondition />
            </ProtectedRoute>} />
            <Route path="/messages/" element={<ProtectedRoute allowedRoles={['PATIENT', 'DOCTOR', 'STAFF']}>
              <MessagePage />
            </ProtectedRoute>} />
            <Route path="/messages/new_message/:receiverId" element={<ProtectedRoute allowedRoles={['PATIENT', 'DOCTOR', 'STAFF']}>
              <AddMessage />
            </ProtectedRoute>} />
            <Route path="/images/:patientId" element={<ProtectedRoute allowedRoles={['DOCTOR']}>
              <ImagesPage />
            </ProtectedRoute>} />
            <Route path="/edit-image/:patientId/:imageId" element={<ProtectedRoute allowedRoles={['DOCTOR']}>
              <ImagesPage />
            </ProtectedRoute>} />
          </Routes>
      </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
