package org.PVH.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CustomerDTO {

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String vorname;

    private String nachname;

    private String email;

    public CustomerDTO(String vorname, String nachname, String email) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
    }
}
