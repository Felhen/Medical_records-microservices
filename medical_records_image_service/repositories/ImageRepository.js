const Image = require("../models/ImageModel");

class ImageRepository {
  static async getImagesByPatient(patientId) {
    const images = await Image.findAll({
      where: { patient_id: parseInt(patientId, 10) },
      order: [['uploaded_at', 'DESC']],
    });

    return images;
  }

  static async uploadImage(filename, patient_id, doctor_id) {
    const filePath = `/uploads/${filename}`;

    const image = await Image.create({
      patient_id: parseInt(patient_id, 10),
      doctor_id: parseInt(doctor_id, 10),
      filename,
      path: filePath,
    });

    return image;
  }

  static async updateImagePath(oldPath, newPath) {
    const [affectedRows] = await Image.update(
      { path: newPath },
      { where: { path: oldPath } }
    );

    if (affectedRows === 0) {
      throw new Error("No image was updated. Make sure the old path exists.");
    }

    return { oldPath, newPath };
  }
}

module.exports = ImageRepository;
