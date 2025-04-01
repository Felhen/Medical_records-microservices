package se.kth.felixhr.medical_records.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "t_encounter")
public class Encounter {

    private Long id;
    private Date encounter_date;
    private String encounter_info;
    private Long patientId;
    private Long doctorId;


    public Encounter(Date encounter_date, String encounter_info, Long patientId, Long doctorId) {
        this.encounter_date = encounter_date;
        this.encounter_info = encounter_info;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public Encounter() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "encounter_id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "encounter_date", nullable = false)
    public Date getEncounter_date() {
        return encounter_date;
    }
    public void setEncounter_date(Date encounter_date) {
        this.encounter_date = encounter_date;
    }

    @Column(name = "encounter_info", nullable = false)
    public String getEncounter_info() {
        return encounter_info;
    }
    public void setEncounter_info(String encounter_info) {
        this.encounter_info = encounter_info;
    }

    @Column(name = "patient_id", nullable = false)
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Column(name = "doctor_id", nullable = false)
    public Long getDoctorId(){ return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

}
