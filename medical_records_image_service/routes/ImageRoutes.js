const express = require("express");
const router = express.Router();
const multer = require("multer");
const path = require("path");
const ImageController = require("../controllers/ImageController");

// Multer Configuration for File Uploads
const storage = multer.diskStorage({
  destination: "uploads/",
  filename: (req, file, cb) => {
    cb(null, Date.now() + path.extname(file.originalname));
  }
});
const upload = multer({ 
  storage: storage,
  limits: { fileSize: 100 * 1024 * 1024 }, // 100 MB limit
});


module.exports = (keycloak) => {
  const router = express.Router();

  router.post("/upload", keycloak.protect(['DOCTOR', 'STAFF']), upload.single("file"), ImageController.uploadImage);
  router.post("/edit", keycloak.protect(['DOCTOR', 'STAFF']), upload.single("file"), ImageController.editImage);
  router.get("/:patientId", keycloak.protect(['DOCTOR', 'STAFF']), ImageController.getImagesByPatient);

  return router;
};
