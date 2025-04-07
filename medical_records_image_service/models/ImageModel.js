const { DataTypes } = require('sequelize');
const sequelize = require('../config/db');

const Image = sequelize.define('Image', {
  image_id: {
    type: DataTypes.BIGINT,
    primaryKey: true,
    autoIncrement: true
  },
  patient_id: {
    type: DataTypes.BIGINT,
    allowNull: false,
    references: {
      model: 't_patient', // table name
      key: 'patient_id'
    },
    onDelete: 'CASCADE'
  },
  doctor_id: {
    type: DataTypes.BIGINT,
    allowNull: false,
    references: {
      model: 't_user',
      key: 'user_id'
    },
    onDelete: 'CASCADE'
  },
  filename: {
    type: DataTypes.STRING(255),
    allowNull: false
  },
  path: {
    type: DataTypes.STRING(255),
    allowNull: false
  },
  uploaded_at: {
    type: DataTypes.DATE,
    defaultValue: DataTypes.NOW
  }
}, {
  tableName: 't_image',
  timestamps: false
});

module.exports = Image;
