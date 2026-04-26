package it.alex89.usermgr;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.excp.ValidationException;
import it.alex89.usermgr.model.UserEdit;
import it.alex89.usermgr.repository.RolesTypeRepository;
import it.alex89.usermgr.repository.UsersRepository;
import it.alex89.usermgr.service.UsersService;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(UsersService.class)
public class UsersServiceEditUserUT {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;
    @Autowired
    private EntityManager entityManager;

    private UsersEntity usersEntity1, usersEntity2;

    @Test
    public void userIdExistAndChangeRoleWithEmptyShouldEditUserEntity() throws Exception {
        UserEdit userEdit = new UserEdit();
        userEdit.setUsername("username_edit");
        userEdit.setName("name_edit");
        userEdit.setSurname("surname_edit");
        userEdit.setTaxcode("taxcode_edit");
        userEdit.setRoles(Collections.emptyList());
        usersService.editUser(usersEntity1.getIdUser(), userEdit, "TEST","ADMIN");

        entityManager.flush();
        entityManager.clear();

        Optional<UsersEntity> res = usersRepository.findById(usersEntity1.getIdUser());

        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getIdUser()).isEqualTo(usersEntity1.getIdUser());
        assertThat(res.get().getEmail()).isEqualTo(usersEntity1.getEmail());
        assertThat(res.get().getUsername()).isEqualTo("username_edit");
        assertThat(res.get().getName()).isEqualTo("name_edit");
        assertThat(res.get().getSurname()).isEqualTo("surname_edit");
        assertThat(res.get().getTaxCode()).isEqualTo("taxcode_edit");
        assertThat(res.get().getRoles()).hasSize(0);
    }

    @Test
    public void userIdExistAndChangeRoleShouldEditUserEntity() throws Exception {
        UserEdit userEdit = new UserEdit();
        userEdit.setUsername("username_edit");
        userEdit.setName("name_edit");
        userEdit.setSurname("surname_edit");
        userEdit.setTaxcode("taxcode_edit");
        userEdit.setRoles(List.of("OPERATOR"));
        usersService.editUser(usersEntity1.getIdUser(), userEdit, "TEST","ADMIN");
        Optional<UsersEntity> res = usersRepository.findById(usersEntity1.getIdUser());

        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getIdUser()).isEqualTo(usersEntity1.getIdUser());
        assertThat(res.get().getEmail()).isEqualTo(usersEntity1.getEmail());
        assertThat(res.get().getUsername()).isEqualTo("username_edit");
        assertThat(res.get().getName()).isEqualTo("name_edit");
        assertThat(res.get().getSurname()).isEqualTo("surname_edit");
        assertThat(res.get().getTaxCode()).isEqualTo("taxcode_edit");
        assertThat(res.get().getRoles()).hasSize(1);

        assertThat(res.get().getRoles().getFirst().getRoleType().getRoleName()).isEqualTo("OPERATOR");
    }

    @Test(expected = NotFoundException.class)
    public void userIdNotExistAndShouldNotEditUserEntity() throws Exception {
        UserEdit userEdit = new UserEdit();
        userEdit.setUsername("username_edit");
        userEdit.setName("name_edit");
        userEdit.setSurname("surname_edit");
        userEdit.setTaxcode("taxcode_edit");
        userEdit.setRoles(Collections.emptyList());
        usersService.editUser("NOT_EXIST", userEdit, "TEST","ADMIN");
    }

    @After
    public void cleanData() {
        usersRepository.deleteAll();
        rolesTypeRepository.deleteAll();
    }

    @Before
    public void setUp() {
        RoleTypeEntity developerRole = new RoleTypeEntity();
        developerRole.setRoleName("DEVELOPER");
        developerRole = rolesTypeRepository.saveAndFlush(developerRole);
        RoleTypeEntity maintainerRole = new RoleTypeEntity();
        maintainerRole.setRoleName("MAINTAINER");
        maintainerRole = rolesTypeRepository.saveAndFlush(maintainerRole);
        RoleTypeEntity operatorRole = new RoleTypeEntity();
        operatorRole.setRoleName("OPERATOR");
        operatorRole = rolesTypeRepository.saveAndFlush(operatorRole);
        RoleTypeEntity ownerRole = new RoleTypeEntity();
        ownerRole.setRoleName("OWNER");
        ownerRole = rolesTypeRepository.saveAndFlush(ownerRole);

        usersEntity1 = new UsersEntity();
        usersEntity1.setEmail("test@test.it");
        usersEntity1.setUsername("username");
        usersEntity1.setName("name");
        usersEntity1.setSurname("surname");
        usersEntity1.setTaxCode("taxcode");
        usersEntity1.setDateLastUpdate(new Date());
        usersEntity1.setUserIdLastUpdate("SYSTEM");

        RolesEntity usersEntity1Role = new RolesEntity();
        usersEntity1Role.setUser(usersEntity1);
        usersEntity1Role.setRoleType(maintainerRole);
        usersEntity1.addRole(usersEntity1Role);

        usersEntity1 = usersRepository.saveAndFlush(usersEntity1);

        usersEntity2 = new UsersEntity();
        usersEntity2.setEmail("test1@test.it");
        usersEntity2.setUsername("username");
        usersEntity2.setName("name");
        usersEntity2.setSurname("surname");
        usersEntity2.setTaxCode("taxcode");
        usersEntity2.setDateLastUpdate(new Date());
        usersEntity2.setUserIdLastUpdate("SYSTEM");

        RolesEntity usersEntity2Role = new RolesEntity();
        usersEntity2Role.setUser(usersEntity2);
        usersEntity2Role.setRoleType(developerRole);
        usersEntity2.addRole(usersEntity2Role);

        usersEntity2 = usersRepository.saveAndFlush(usersEntity2);
    }
}
