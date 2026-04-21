package it.alex89.usermgr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rolestype")
public class RoleTypeEntity {

    @Id
    @Column(name = "ROLENAME", length = 100, nullable = false)
    private String roleName;

    public RoleTypeEntity() {}

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
