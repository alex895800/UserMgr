package it.alex89.usermgr.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "TAXCODE")
    private String taxCode;

    @Column(name = "DATELASTUPDATE")
    private Date dateLastUpdate;

    @Column(name = "USERIDLASTUPDATE")
    private String userIdLastUpdate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolesEntity> roles;

    public UsersEntity() {}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getTaxCode() { return taxCode; }

    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }

    public Date getDateLastUpdate() { return dateLastUpdate; }

    public void setDateLastUpdate(Date dateLastUpdate) { this.dateLastUpdate = dateLastUpdate; }

    public String getUserIdLastUpdate() { return userIdLastUpdate; }

    public void setUserIdLastUpdate(String userIdLastUpdate) { this.userIdLastUpdate = userIdLastUpdate; }

    public List<RolesEntity> getRoles() { return roles; }

    public void setRoles(List<RolesEntity> roles) { this.roles = roles; }

    public void addRole(RolesEntity role) {
        if(this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
        role.setUser(this);
    }

    public void removeRole(RolesEntity role) {
        this.roles.remove(role);
        role.setUser(null);
    }
}
