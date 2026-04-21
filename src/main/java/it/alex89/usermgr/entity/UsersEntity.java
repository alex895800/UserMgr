package it.alex89.usermgr.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "IDUSER", length = 36, updatable = false, nullable = false)
    private String idUser;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "USERNAME", length = 100, nullable = false)
    private String username;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "SURNAME", length = 100, nullable = false)
    private String surname;

    @Column(name = "TAXCODE", length = 100, nullable = false)
    private String taxCode;

    @Column(name = "DATELASTUPDATE", nullable = false)
    private Date dateLastUpdate;

    @Column(name = "USERIDLASTUPDATE", length = 100, nullable = false)
    private String userIdLastUpdate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolesEntity> roles;

    public UsersEntity() {}

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Date getDateLastUpdate() {
        return dateLastUpdate;
    }

    public void setDateLastUpdate(Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getUserIdLastUpdate() {
        return userIdLastUpdate;
    }

    public void setUserIdLastUpdate(String userIdLastUpdate) {
        this.userIdLastUpdate = userIdLastUpdate;
    }

    public List<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesEntity> roles) {
        this.roles = roles;
    }

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
