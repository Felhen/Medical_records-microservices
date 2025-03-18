import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../context/AuthProvider";
import ImageEditor from "../entities/ImageEditor";

const ImagesPage = () => {
  const { userId } = useAuth();
  const { patientId } = useParams();
  const [images, setImages] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);
  

  useEffect(() => {
    fetchImages();
  }, []);

  const fetchImages = async () => {
    const response = await fetch(`http://localhost:5000/images/${patientId}`);
    const data = await response.json();
    setImages(data.images || []);
  };

  const handleUpload = async (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    formData.append("patient_id", patientId);
    formData.append("doctor_id", userId);

    console.log([...formData.entries()]); // Log form data fields

    const response = await fetch("http://localhost:5000/images/upload", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      const newImage = await response.json();
      setImages((prev) => [newImage.image, ...prev]);
    }
  };

  return (
    <div>
      <h2>Manage Images for Patient {patientId}</h2>
      <input type="file" onChange={handleUpload} />
      <div>
        {images.length > 0 ? (
          images.map((image) => (
            <div key={image.image_id}>
              <img 
                src={`http://localhost:5000${image.path}`} 
                alt="Patient Scan" 
                width="300px" 
              />
              <button onClick={() => setSelectedImage(image.path)}>Edit</button>
            </div>
          ))
        ) : (
          <p>No images found. Upload one to start editing.</p>
        )}
      </div>

      {selectedImage && <ImageEditor imageUrl={`http://localhost:5000${selectedImage}`} />}
    </div>
  );
};

export default ImagesPage;

