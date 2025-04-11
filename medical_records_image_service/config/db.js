const { Sequelize } = require('sequelize');

const sequelize = new Sequelize(
  process.env.DB_NAME || 'medical_records',
  process.env.DB_USER || 'felix',
  process.env.DB_PASSWORD || 'secret',
  {
    host: process.env.DB_HOST || 'vm.cloud.cbh.kth.se',
    port: process.env.DB_PORT || 20074,
    dialect: 'mysql'
  }
);

module.exports = sequelize;
