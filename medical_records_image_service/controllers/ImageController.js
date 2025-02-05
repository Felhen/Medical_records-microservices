const path = require("path");
const fs = require("fs");
const ImageRepository = require("../repositories/ImageRepository");

const handleImageUpload = async (req, res, isEdit = false) => {
  const { patient_id, doctor_id } = req.body;

  if (!req.file || !patient_id || !doctor_id) {
    return res.status(400).json({ error: "Missing file or required parameters." });
  }

  try {
    const image = await ImageRepository.uploadImage(req.file.filename, patient_id, doctor_id);
    
    if (isEdit) {
      res.json({ message: "Image edited and saved as a new record!", image });
    } else {
      res.json({ message: "Image uploaded!", image });
    }
  } catch (err) {
    res.status(500).json({ error: "Error processing image", details: err });
  }
};

const uploadImage = async (req, res) => {
  return handleImageUpload(req, res, false);
};

const editImage = async (req, res) => {
  return handleImageUpload(req, res, true);
};

const getImagesByPatient = async (req, res) => {
  const { patientId } = req.params;
  try {
    const images = await ImageRepository.getImagesByPatient(patientId);
    res.json({ message: "Images found", images });
  } catch (err) {
    res.status(500).json({ error: "Error retrieving images", details: err });
  }
};

module.exports = { uploadImage, editImage, getImagesByPatient };
