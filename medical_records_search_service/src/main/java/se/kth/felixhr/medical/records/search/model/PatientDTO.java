package se.kth.felixhr.medical.records.search.model;

import java.sql.Date;

public class PatientDTO {
    public Long patientId;
    public String firstName;
    public String lastName;
    public Date birthDate;
    public int personNumber;

    public PatientDTO(Long patientId, String firstName, String lastName, Date birthDate, int personNumber) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.personNumber = personNumber;
    }
}

