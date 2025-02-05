const pool = require("../config/db");
const Image = require("../models/ImageModel");

class ImageRepository {
  static async getImagesByPatient(patientId) {
    const [rows] = await pool.execute(
      "SELECT * FROM t_image WHERE patient_id = ? ORDER BY uploaded_at DESC",
      [patientId]
    );

    return rows.map(
      (row) => new Image(row.image_id, row.patient_id, row.doctor_id, row.filename, row.path, row.uploaded_at)
    );
  }

  static async uploadImage(filename, patient_id, doctor_id) {
    const filePath = `/uploads/${filename}`;

    const [result] = await pool.execute(
      "INSERT INTO t_image (patient_id, doctor_id, filename, path) VALUES (?, ?, ?, ?)",
      [patient_id, doctor_id, filename, filePath]
    );

    return new Image(result.insertId, patient_id, doctor_id, filename, filePath, new Date());
  }

  static async updateImagePath(oldPath, newPath) {
    const [result] = await pool.execute(
      "UPDATE t_image SET path = ? WHERE path = ?",
      [newPath, oldPath]
    );

    if (result.affectedRows === 0) {
      throw new Error("No image was updated. Make sure the old path exists.");
    }

    return { oldPath, newPath };
  }
}

module.exports = ImageRepository;


