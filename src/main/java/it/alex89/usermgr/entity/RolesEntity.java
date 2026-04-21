package it.alex89.usermgr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RolesEntity {

    @Id
    @Column(name = "IDROLES")
    private String idRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL", nullable = false)
    private UsersEntity user;

    @Column(name = "ROLENAME", nullable = false)
    private String roleName;

    public RolesEntity() {}

    public String getIdRoles() { return idRoles; }

    public void setIdRoles(String idRoles) { this.idRoles = idRoles; }

    public UsersEntity getUser() { return user; }

    public void setUser(UsersEntity user) { this.user = user; }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName; }
}
