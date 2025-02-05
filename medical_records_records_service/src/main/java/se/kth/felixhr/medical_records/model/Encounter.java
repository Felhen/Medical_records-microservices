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


    public Encounter(Date encounter_date, String encounter_info, Long patientId) {
        this.encounter_date = encounter_date;
        this.encounter_info = encounter_info;
        this.patientId = patientId;
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

    @Column(name = "encounter_date")
    public Date getEncounter_date() {
        return encounter_date;
    }
    public void setEncounter_date(Date encounter_date) {
        this.encounter_date = encounter_date;
    }

    @Column(name = "encounter_info")
    public String getEncounter_info() {
        return encounter_info;
    }
    public void setEncounter_info(String encounter_info) {
        this.encounter_info = encounter_info;
    }

    @Column(name = "patient_id")
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }


/*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    public void setPatient(Patient patient) {
        this.patient = patient;
    }*/
}
