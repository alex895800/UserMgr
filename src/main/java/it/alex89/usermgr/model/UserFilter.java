package it.alex89.usermgr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilter {
    private String email;
    private String username;
    private String name;
    private String surname;
    private String taxcode;
    private String role;

    public UserFilter() {

    }

    public UserFilter(String email, String username, String name, String surname, String taxcode, String role) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.taxcode = taxcode;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
