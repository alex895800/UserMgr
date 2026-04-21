package it.alex89.usermgr.repository;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesTypeRepository extends JpaRepository<RoleTypeEntity, String> {
}
