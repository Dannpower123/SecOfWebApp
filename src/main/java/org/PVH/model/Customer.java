package org.PVH.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customer", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    @NotBlank
    @Size(max = 30)
    private String vorname;

    @NotBlank
    @Size(max = 30)
    private String nachname;

    @Email
    @NotBlank
    @Size(max = 120)
    private String email;

    public Customer(String vorname, String nachname, String email) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
    }

    public Customer() {

    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }
}
