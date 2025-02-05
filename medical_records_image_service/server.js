const cors = require("cors");

require("dotenv").config();
const express = require("express");
const ImageRoutes = require("./routes/ImageRoutes");

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());
app.use("/uploads", express.static("uploads")); // Serve images
app.use("/images", ImageRoutes); // Image API routes
app.use(express.json({ limit: '100mb' }));
app.use(express.urlencoded({ limit: '100mb', extended: true }));

app.listen(PORT, () => console.log(`Image Service running on port ${PORT}`));
