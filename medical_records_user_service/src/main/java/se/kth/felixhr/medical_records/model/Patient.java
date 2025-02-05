package se.kth.felixhr.medical_records.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name = "t_patient")
public class Patient  {

    private Long id;
    private String first_name;
    private String last_name;
    private Date birth_date;
    private int person_number;
    private User user;

    public Patient(String first_name, String last_name, Date birth_date, int person_number, User user) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.person_number = person_number;
        this.user = user;
    }

    public Patient() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "patient_id")
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    @Column(name= "first_name")
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Column(name= "last_name")
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Column(name= "birth_date")
    public Date getBirth_date() {
        return birth_date;
    }
    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    @Column(name= "person_number", unique = true)
    public int getPerson_number() {
        return person_number;
    }
    public void setPerson_number(int person_number) {
        this.person_number = person_number;
    }


    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
