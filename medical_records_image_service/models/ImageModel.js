class Image {
    constructor(image_id, patient_id, doctor_id, filename, path, uploaded_at) {
      this.image_id = image_id;
      this.patient_id = patient_id;
      this.doctor_id = doctor_id;
      this.filename = filename;
      this.path = path;
      this.uploaded_at = uploaded_at;
    }
  }
  
  module.exports = Image;
  