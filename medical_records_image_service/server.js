const cors = require("cors");
const sequelize = require('./config/db'); // Make sure this points to your Sequelize instance

// Connect to DB and sync the Image model
sequelize.sync()
  .then(() => console.log("Database synced"))
  .catch(err => console.error("Sequelize sync error:", err));

require("dotenv").config();
const express = require("express");
const Keycloak = require("keycloak-connect");

const app = express();
const PORT = process.env.PORT || 5000;

const keycloak = new Keycloak({}, {
  "realm": "medical-records",
  "auth-server-url": "http://felixh-keycloak.cloud.cbh.kth.se/",
  "ssl-required": "none",
  "resource": "medical_app",
  "bearer-only": true,
  "confidential-port": 0,
});


app.use(cors({
  origin: 'https://felixhr-front.cloud.cbh.kth.se',
  credentials: true,
  exposedHeaders: ['Authorization']
}));

app.use(express.json());
app.use("/uploads", express.static("uploads")); // Serve images
app.use(express.json({ limit: '100mb' }));
app.use(express.urlencoded({ limit: '100mb', extended: true }));

app.use(keycloak.middleware());
const ImageRoutes = require("./routes/ImageRoutes")(keycloak);
app.use("/images", ImageRoutes);

app.listen(PORT, () => console.log(`Image Service running on port ${PORT}`));
