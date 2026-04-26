package it.alex89.usermgr;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.AlreadyCreatedException;
import it.alex89.usermgr.excp.ValidationException;
import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserEdit;
import it.alex89.usermgr.repository.RolesTypeRepository;
import it.alex89.usermgr.repository.UsersRepository;
import it.alex89.usermgr.service.UsersService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class UsersServiceInsertUserUT {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;

    private UsersEntity usersEntity1, usersEntity2;

    @Test
    public void userCorrectlyShouldInsertUserEntity() throws Exception {
        User user = new User();
        user.setEmail("test5@test.it");
        user.setUsername("test_add");
        user.setName("test_add");
        user.setSurname("test_add");
        user.setTaxcode("taxcode_add");
        user.setRoles(List.of("OWNER"));
        user = usersService.insertUser(user, "TEST","ADMIN");

        Optional<UsersEntity> res = usersRepository.findById(user.getId());

        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getIdUser()).isEqualTo(user.getId());
        assertThat(res.get().getEmail()).isEqualTo("test5@test.it");
        assertThat(res.get().getUsername()).isEqualTo("test_add");
        assertThat(res.get().getName()).isEqualTo("test_add");
        assertThat(res.get().getSurname()).isEqualTo("test_add");
        assertThat(res.get().getTaxCode()).isEqualTo("taxcode_add");
        assertThat(res.get().getRoles()).hasSize(1);
        assertThat(res.get().getRoles().getFirst().getRoleType().getRoleName()).isEqualTo("OWNER");
    }

    @Test(expected = ValidationException.class)
    public void userWithInvalidRoleShouldThrowException() throws Exception {
        User user = new User();
        user.setEmail("test2@test.it");
        user.setUsername("test_add");
        user.setName("test_add");
        user.setSurname("test_add");
        user.setTaxcode("taxcode_add");
        user.setRoles(List.of("OWNER1"));
        usersService.insertUser(user, "TEST","ADMIN");
    }

    @Test(expected = ValidationException.class)
    public void userWithEmailInvalidFormatShouldThrowException() throws Exception {
        User user = new User();
        user.setEmail("test.it");
        user.setUsername("test_add");
        user.setName("test_add");
        user.setSurname("test_add");
        user.setTaxcode("taxcode_add");
        user.setRoles(Collections.emptyList());
        usersService.insertUser(user, "TEST","ADMIN");
    }

    @Test(expected = ValidationException.class)
    public void userWithSomeEmptyFieldShouldThrowException() throws Exception {
        User user = new User();
        user.setEmail("test@test.it");
        user.setUsername("");
        user.setName("test_add");
        user.setSurname("test_add");
        user.setTaxcode("taxcode_add");
        user.setRoles(Collections.emptyList());
        usersService.insertUser(user, "TEST","ADMIN");
    }

    @Test(expected = AlreadyCreatedException.class)
    public void userWithEmailAlreadyExistShouldThrowException() throws Exception {
        User user = new User();
        user.setEmail("test@test.it");
        user.setUsername("test_add");
        user.setName("test_add");
        user.setSurname("test_add");
        user.setTaxcode("taxcode_add");
        user.setRoles(Collections.emptyList());
        usersService.insertUser(user, "TEST","ADMIN");
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
        usersEntity2.setUsername("username1");
        usersEntity2.setName("name1");
        usersEntity2.setSurname("surname1");
        usersEntity2.setTaxCode("taxcode1");
        usersEntity2.setDateLastUpdate(new Date());
        usersEntity2.setUserIdLastUpdate("SYSTEM");

        RolesEntity usersEntity2Role = new RolesEntity();
        usersEntity2Role.setUser(usersEntity2);
        usersEntity2Role.setRoleType(developerRole);
        usersEntity2.addRole(usersEntity2Role);

        usersEntity2 = usersRepository.saveAndFlush(usersEntity2);
    }
}
