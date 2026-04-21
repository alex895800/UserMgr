package it.alex89.usermgr.repository;

import it.alex89.usermgr.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity, String> {
}
