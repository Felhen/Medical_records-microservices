const sinon = require("sinon");
const ImageController = require("../controllers/ImageController");
const ImageRepository = require("../repositories/ImageRepository");

describe("ImageController", () => {
  let req, res, sandbox;

  beforeEach(() => {
    sandbox = sinon.createSandbox();
    req = {
      body: {},
      file: null,
      params: {}
    };
    res = {
      status: sinon.stub().returnsThis(),
      json: sinon.stub()
    };
  });

  afterEach(() => sandbox.restore());

  // Upload Success
  it("should upload image successfully", async () => {
    req.body = { patient_id: "1", doctor_id: "2" };
    req.file = { filename: "test.png" };

    const mockImage = { id: 1, filename: "test.png" };
    sandbox.stub(ImageRepository, "uploadImage").resolves(mockImage);

    await ImageController.uploadImage(req, res);

    sinon.assert.calledWith(res.json, { message: "Image uploaded!", image: mockImage });
  });

  // Missing file or IDs
  it("should return 400 if file or ids are missing", async () => {
    req.body = { patient_id: "abc", doctor_id: "2" }; // patient_id not a number
    req.file = null;

    await ImageController.uploadImage(req, res);

    sinon.assert.calledWith(res.status, 400);
    sinon.assert.calledWith(res.json, sinon.match.has("error"));
  });

  // Repo throws error
  it("should return 500 on upload error", async () => {
    req.body = { patient_id: "1", doctor_id: "2" };
    req.file = { filename: "test.png" };

    sandbox.stub(ImageRepository, "uploadImage").throws(new Error("db fail"));

    await ImageController.uploadImage(req, res);

    sinon.assert.calledWith(res.status, 500);
    sinon.assert.calledWith(res.json, sinon.match.has("error"));
  });

  // Edit reuses upload logic
  it("should call upload logic on edit", async () => {
    req.body = { patient_id: "1", doctor_id: "2" };
    req.file = { filename: "edit.png" };

    const mockImage = { id: 9, filename: "edit.png" };
    sandbox.stub(ImageRepository, "uploadImage").resolves(mockImage);

    await ImageController.editImage(req, res);

    sinon.assert.calledWith(res.json, {
      message: "Image edited and saved as a new record!",
      image: mockImage
    });
  });

  // Get Images Success
  it("should return images for valid patient", async () => {
    req.params.patientId = "1";
    const mockImages = [{ image_id: 1 }, { image_id: 2 }];

    sandbox.stub(ImageRepository, "getImagesByPatient").resolves(mockImages);

    await ImageController.getImagesByPatient(req, res);

    sinon.assert.calledWith(res.json, { message: "Images found", images: mockImages });
  });

  // Invalid patient ID
  it("should return 400 on invalid patientId", async () => {
    req.params.patientId = "not-a-number";

    await ImageController.getImagesByPatient(req, res);

    sinon.assert.calledWith(res.status, 400);
    sinon.assert.calledWith(res.json, sinon.match.has("error"));
  }); 

  // Repo throws on getImagesByPatient
  it("should return 500 if repository get fails", async () => {
    req.params.patientId = "1";

    sandbox.stub(ImageRepository, "getImagesByPatient").throws(new Error("DB fail"));

    await ImageController.getImagesByPatient(req, res);

    sinon.assert.calledWith(res.status, 500);
    sinon.assert.calledWith(res.json, sinon.match.has("error"));
  });
});
