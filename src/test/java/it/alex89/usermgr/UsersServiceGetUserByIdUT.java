package it.alex89.usermgr;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.model.User;
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

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(UsersService.class)
public class UsersServiceGetUserByIdUT {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;

    private UsersEntity usersEntity1, usersEntity2;

    @Test
    public void userIdExistShouldGetUserEntity() throws Exception {
        User user = usersService.getUserById(usersEntity1.getIdUser(),"TEST","ADMIN");
        assertThat(user).isNotNull();

        assertThat(user.getId()).isEqualTo(usersEntity1.getIdUser());
        assertThat(user.getEmail()).isEqualTo(usersEntity1.getEmail());
        assertThat(user.getUsername()).isEqualTo(usersEntity1.getUsername());
        assertThat(user.getName()).isEqualTo(usersEntity1.getName());
        assertThat(user.getSurname()).isEqualTo(usersEntity1.getSurname());
        assertThat(user.getTaxcode()).isEqualTo(usersEntity1.getTaxCode());
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles().getFirst()).isEqualTo("MAINTAINER");
    }

    @Test(expected = NotFoundException.class)
    public void userIdNotExistShouldThrowException() throws Exception {
        usersService.getUserById("NOT_EXIST","TEST","ADMIN");
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
