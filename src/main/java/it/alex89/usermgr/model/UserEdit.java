package it.alex89.usermgr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEdit {
    private String username;
    private String name;
    private String surname;
    private String taxcode;
    private List<String> roles;

    public UserEdit() {

    }

    public UserEdit(String username, String name, String surname, String taxcode, List<String> roles) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.taxcode = taxcode;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
