const { Sequelize } = require('sequelize');

const sequelize = new Sequelize(
  process.env.DB_NAME || 'medical_records',
  process.env.DB_USER || 'root',
  process.env.DB_PASSWORD || '1234567',
  {
    host: process.env.DB_HOST || 'mysql',
    dialect: 'mysql'
  }
);

module.exports = sequelize;
