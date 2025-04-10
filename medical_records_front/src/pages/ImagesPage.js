import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../context/AuthProvider";
import ImageEditor from "../entities/ImageEditor";
import securedAxios from "../keycloak/SecuredAxios";

const ImagesPage = () => {
  const { userId } = useAuth();
  const { patientId } = useParams();
  const [images, setImages] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);

  const IMAGE_BASE_URL = process.env.REACT_APP_IMAGE_API || 'http://localhost:5000';

  useEffect(() => {
    fetchImages();
  }, []);

  const fetchImages = async () => {
    try {
      const response = await securedAxios(IMAGE_BASE_URL).get(`/images/${patientId}`);
      setImages(response.data.images || []);
    } catch (error) {
      console.error("Error fetching images:", error);
    }
  };

  const handleUpload = async (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    formData.append("patient_id", patientId);
    formData.append("doctor_id", userId);

    console.log([...formData.entries()]);

    try {
      const response = await securedAxios(IMAGE_BASE_URL).post('/images/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setImages((prev) => [response.data.image, ...prev]);
    } catch (error) {
      console.error("Upload failed:", error);
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
                src={`${IMAGE_BASE_URL}${image.path}`}
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

      {selectedImage && <ImageEditor imageUrl={`${IMAGE_BASE_URL}${selectedImage}`} />}
    </div>
  );
};

export default ImagesPage;
