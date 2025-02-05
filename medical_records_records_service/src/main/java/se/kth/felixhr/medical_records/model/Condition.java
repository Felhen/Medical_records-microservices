package se.kth.felixhr.medical_records.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "t_condition")
public class Condition  {

    private Long id;
    private String condition_name;
    private String condition_info;
    private Date condition_date;
    private Long patientId;

    public Condition(String condition_name, String condition_info, Date condition_date, Long patientId) {
        this.condition_name = condition_name;
        this.condition_info = condition_info;
        this.condition_date = condition_date;
        this.patientId = patientId;
    }

    public Condition() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "condition_id")
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    @Column(name= "condition_name")
    public String getCondition_name() {
        return condition_name;
    }
    public void setCondition_name(String condition_name) {
        this.condition_name = condition_name;
    }

    @Column(name= "condition_info")
    public String getCondition_info() {
        return condition_info;
    }
    public void setCondition_info(String condition_info) {
        this.condition_info = condition_info;
    }

    @Column(name= "condition_date")
    public Date getCondition_date() {
        return condition_date;
    }
    public void setCondition_date(Date condition_date) {
        this.condition_date = condition_date;
    }

    @Column(name = "patient_id")
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
