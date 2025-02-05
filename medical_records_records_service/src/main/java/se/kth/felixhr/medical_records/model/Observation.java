package se.kth.felixhr.medical_records.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "t_observation")
public class Observation {

    private Long id;
    private Date observation_date;
    private String observation_info;
    private Long patientId;

    public Observation(Date observation_date, String observation_info, Long patientId) {
        this.observation_date = observation_date;
        this.observation_info = observation_info;
        this.patientId = patientId;
    }

    public Observation() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "observation_id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "observation_date")
    public Date getObservation_date() {
        return observation_date;
    }
    public void setObservation_date(Date observation_date) {
        this.observation_date = observation_date;
    }

    @Column(name = "observation_info")
    public String getObservation_info() {
        return observation_info;
    }
    public void setObservation_info(String observation_info) {
        this.observation_info = observation_info;
    }

   @Column(name = "patient_id")
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
