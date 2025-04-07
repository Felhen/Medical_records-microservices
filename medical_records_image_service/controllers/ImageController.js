const path = require("path");
const fs = require("fs");
const ImageRepository = require("../repositories/ImageRepository");

const handleImageUpload = async (req, res, isEdit = false) => {
  const patientId = parseInt(req.body.patient_id, 10);
  const doctorId = parseInt(req.body.doctor_id, 10);

  if (!req.file || isNaN(patientId) || isNaN(doctorId)) {
    return res.status(400).json({ error: "Missing file or required numeric parameters." });
  }

  try {
    const image = await ImageRepository.uploadImage(req.file.filename, patientId, doctorId);

    if (isEdit) {
      res.json({ message: "Image edited and saved as a new record!", image });
    } else {
      res.json({ message: "Image uploaded!", image });
    }
  } catch (err) {
    console.error("Error processing image upload:", err);
    res.status(500).json({ error: "Error processing image", details: err.message });
  }
};

const uploadImage = async (req, res) => {
  return handleImageUpload(req, res, false);
};

const editImage = async (req, res) => {
  return handleImageUpload(req, res, true);
};

const getImagesByPatient = async (req, res) => {
  const patientId = parseInt(req.params.patientId, 10);
  console.log("Fetching images for patient:", patientId);

  if (isNaN(patientId)) {
    return res.status(400).json({ error: "Invalid patient ID" });
  }

  try {
    const images = await ImageRepository.getImagesByPatient(patientId);
    console.log("Found images:", images);
    res.json({ message: "Images found", images });
  } catch (err) {
    console.error("Error retrieving images:", err);
    res.status(500).json({ error: "Error retrieving images", details: err.message });
  }
};

module.exports = { uploadImage, editImage, getImagesByPatient };
