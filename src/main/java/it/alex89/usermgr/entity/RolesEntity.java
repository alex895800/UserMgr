package it.alex89.usermgr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "IDROLES", length = 36, updatable = false, nullable = false)
    private String idRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSER", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLENAME", nullable = false)
    private RoleTypeEntity roleType;

    public RolesEntity() {}

    public String getIdRoles() {
        return idRoles;
    }

    public void setIdRoles(String idRoles) {
        this.idRoles = idRoles;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public RoleTypeEntity getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEntity roleType) {
        this.roleType = roleType;
    }
}
