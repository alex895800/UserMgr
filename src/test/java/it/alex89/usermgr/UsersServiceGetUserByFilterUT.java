package it.alex89.usermgr;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserFilter;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(UsersService.class)
public class UsersServiceGetUserByFilterUT {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;

    private UsersEntity usersEntity1, usersEntity2;

    @Test
    public void requestWithoutFilterShouldGetAllSavedUser() throws Exception {
        UserFilter filter = new UserFilter();
        filter.setEmail(null);
        filter.setUsername(null);
        filter.setName(null);
        filter.setSurname(null);
        filter.setTaxcode(null);
        filter.setRole(null);
        List<User> userList = usersService.getUserByFilter(filter,"TEST","ADMIN");

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(2);

        assertThat(userList.getFirst().getId()).isEqualTo(usersEntity1.getIdUser());
        assertThat(userList.getFirst().getEmail()).isEqualTo(usersEntity1.getEmail());
        assertThat(userList.getFirst().getUsername()).isEqualTo(usersEntity1.getUsername());
        assertThat(userList.getFirst().getName()).isEqualTo(usersEntity1.getName());
        assertThat(userList.getFirst().getSurname()).isEqualTo(usersEntity1.getSurname());
        assertThat(userList.getFirst().getTaxcode()).isEqualTo(usersEntity1.getTaxCode());
        assertThat(userList.getFirst().getRoles()).hasSize(1);
        assertThat(userList.getFirst().getRoles().getFirst()).isEqualTo("MAINTAINER");

        assertThat(userList.get(1).getId()).isEqualTo(usersEntity2.getIdUser());
        assertThat(userList.get(1).getEmail()).isEqualTo(usersEntity2.getEmail());
        assertThat(userList.get(1).getUsername()).isEqualTo(usersEntity2.getUsername());
        assertThat(userList.get(1).getName()).isEqualTo(usersEntity2.getName());
        assertThat(userList.get(1).getSurname()).isEqualTo(usersEntity2.getSurname());
        assertThat(userList.get(1).getTaxcode()).isEqualTo(usersEntity2.getTaxCode());
        assertThat(userList.get(1).getRoles()).hasSize(1);
        assertThat(userList.get(1).getRoles().getFirst()).isEqualTo("DEVELOPER");
    }

    @Test
    public void requestWithNameFilterShouldGetAllSavedUser() throws Exception {
        UserFilter filter = new UserFilter();
        filter.setEmail(null);
        filter.setUsername(null);
        filter.setName("name");
        filter.setSurname(null);
        filter.setTaxcode(null);
        filter.setRole(null);
        List<User> userList = usersService.getUserByFilter(filter,"TEST","ADMIN");

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(1);

        assertThat(userList.getFirst().getId()).isEqualTo(usersEntity1.getIdUser());
        assertThat(userList.getFirst().getEmail()).isEqualTo(usersEntity1.getEmail());
        assertThat(userList.getFirst().getUsername()).isEqualTo(usersEntity1.getUsername());
        assertThat(userList.getFirst().getName()).isEqualTo(usersEntity1.getName());
        assertThat(userList.getFirst().getSurname()).isEqualTo(usersEntity1.getSurname());
        assertThat(userList.getFirst().getTaxcode()).isEqualTo(usersEntity1.getTaxCode());
        assertThat(userList.getFirst().getRoles()).hasSize(1);
        assertThat(userList.getFirst().getRoles().getFirst()).isEqualTo("MAINTAINER");
    }

    @Test
    public void userNotExistShouldGetUserEmptyList() throws Exception {
        UserFilter filter = new UserFilter();
        filter.setEmail("notexist@test.it");
        filter.setUsername(null);
        filter.setName(null);
        filter.setSurname(null);
        filter.setTaxcode(null);
        filter.setRole(null);
        List<User> userList = usersService.getUserByFilter(filter,"TEST","ADMIN");

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(0);
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
