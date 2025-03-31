package se.kth.felixhr.medical_records.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_user")
public class User {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private Patient patient;
    private String keycloakId;

    public User(String username, String email, Role role, String keycloakId) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.keycloakId = keycloakId;
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {this.email = email;}

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    public Role getRole() { return role; }
    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "keycloak_id" )
    public String getKeycloakId() {return keycloakId;}
    public void setKeycloakId(String keycloakId) {this.keycloakId = keycloakId; }
}
