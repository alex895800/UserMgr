package it.alex89.usermgr.repository;

import it.alex89.usermgr.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    @Query("SELECT u FROM UsersEntity u WHERE " +
            "(:email IS NULL OR :email = '' OR u.email = :email) AND " +
            "(:username IS NULL OR :username = '' OR u.username = :username) AND " +
            "(:name IS NULL OR :name = '' OR u.name = :name) AND " +
            "(:surname IS NULL OR :surname = '' OR u.surname = :surname) AND " +
            "(:taxcode IS NULL OR :taxcode = '' OR u.taxCode = :taxcode)"
    )
    List<UsersEntity> findByFilter(
            @Param("email") String email,
            @Param("username") String username,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("taxcode") String taxcode
    );


}
