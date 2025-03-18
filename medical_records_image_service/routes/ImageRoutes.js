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

router.post("/upload", upload.single("file"), ImageController.uploadImage);
router.post("/edit", upload.single("file"), ImageController.editImage);
router.get("/:patientId", ImageController.getImagesByPatient);

module.exports = router;
