package it.alex89.usermgr.service;

import it.alex89.usermgr.entity.RoleTypeEntity;
import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.AlreadyCreatedException;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.excp.ValidationException;
import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserEdit;
import it.alex89.usermgr.model.UserFilter;
import it.alex89.usermgr.repository.RolesRepository;
import it.alex89.usermgr.repository.RolesTypeRepository;
import it.alex89.usermgr.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;

    @Transactional
    public User insertUser(User user, String username, String userrole) throws Exception {
        logger.info("Start insert user operation by username {} with role {}", username, userrole);

        if(isFieldUserEmpty(user)){
            throw new ValidationException("The all or part of fields are empty");
        }
        if(!isValidEmail(user.getEmail())){
            throw new ValidationException("The mail address is not valid format");
        }

        List<UsersEntity> users = usersRepository.findByFilter(
                        user.getEmail(),
                        null,
                        null,
                        null,
                        null,
                null);

        if(users == null || users.isEmpty()){
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setEmail(user.getEmail());
            usersEntity.setUsername(user.getUsername());
            usersEntity.setName(user.getName());
            usersEntity.setSurname(user.getSurname());
            usersEntity.setTaxCode(user.getTaxcode());
            usersEntity.setDateLastUpdate(new Date());
            usersEntity.setUserIdLastUpdate(username);

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                for (String roleName : user.getRoles()) {
                    RoleTypeEntity role = rolesTypeRepository.findById(roleName)
                            .orElseThrow(() -> new ValidationException("User with email: " + user.getEmail()
                                    + " role name " + roleName + " not valid"));

                    RolesEntity newRole = new RolesEntity();
                    newRole.setUser(usersEntity);
                    newRole.setRoleType(role);
                    usersEntity.addRole(newRole);
                }
            }

            UsersEntity usersSaved = usersRepository.save(usersEntity);

            logger.info("Finish insert user operation by username {} with role {}", username, userrole);

            return convertFromEntityToId(usersSaved);
        }else{
            throw new AlreadyCreatedException("User with email: " + user.getEmail() + " already inserted");
        }
    }

    @Transactional
    public User editUser(String id, UserEdit user, String username, String userrole) throws Exception {
        logger.info("Start update user operation by username {} with role {}", username, userrole);

        UsersEntity usersEntity = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if(!StringUtils.isEmpty(user.getUsername()))
            usersEntity.setUsername(user.getUsername());
        if(!StringUtils.isEmpty(user.getName()))
            usersEntity.setName(user.getName());
        if(!StringUtils.isEmpty(user.getSurname()))
            usersEntity.setSurname(user.getSurname());
        if(!StringUtils.isEmpty(user.getTaxcode()))
            usersEntity.setTaxCode(user.getTaxcode());
        usersEntity.setDateLastUpdate(new Date());
        usersEntity.setUserIdLastUpdate(username);

        usersEntity.getRoles().clear();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (String roleName : user.getRoles()) {
                RoleTypeEntity role = rolesTypeRepository.findById(roleName)
                        .orElseThrow(() -> new ValidationException("User with email: " + usersEntity.getEmail()
                                + " role name " + roleName + " not valid"));

                RolesEntity newRole = new RolesEntity();
                newRole.setUser(usersEntity);
                newRole.setRoleType(role);
                usersEntity.addRole(newRole);
            }
        }

        UsersEntity usersSaved = usersRepository.save(usersEntity);

        logger.info("Finish update user operation by username {} with role {}", username, userrole);

        return convertFromEntityToObject(usersSaved, userrole);
    }

    @Transactional
    public User editRole(String id, List<String> roles, String username, String userrole) throws Exception {
        logger.info("Start update user role operation by username {} with role {}", username, userrole);

        UsersEntity usersEntity = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        usersEntity.getRoles().clear();
        if (roles != null && !roles.isEmpty()) {
            for (String roleName : roles) {
                RoleTypeEntity role = rolesTypeRepository.findById(roleName)
                        .orElseThrow(() -> new ValidationException("User with email: " + usersEntity.getEmail()
                                + " role name " + roleName + " not valid"));

                RolesEntity newRole = new RolesEntity();
                newRole.setUser(usersEntity);
                newRole.setRoleType(role);
                usersEntity.addRole(newRole);
            }
        }

        usersEntity.setDateLastUpdate(new Date());
        usersEntity.setUserIdLastUpdate(username);

        UsersEntity usersSaved = usersRepository.save(usersEntity);

        logger.info("Finish update user role operation by username {} with role {}", username, userrole);

        return convertFromEntityToObject(usersSaved, userrole);
    }

    public void deleteUser(String id, String username, String userrole) throws Exception {
        logger.info("Start delete user operation by username {} with role {}", username, userrole);

        UsersEntity userLoaded = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        usersRepository.delete(userLoaded);

        logger.info("Finish delete user operation by username {} with role {}", username, userrole);
    }

    public User getUserById(String id, String username, String userrole) throws Exception {
        logger.info("Start get user by id operation by username {} with role {}", username, userrole);

        User user = usersRepository.findById(id)
                .map(userEntity -> convertFromEntityToObject(userEntity, userrole))
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        logger.info("Finish get user by id operation by username {} with role {}", username, userrole);
        return user;
    }

    public List<User> getUserByFilter(UserFilter filter, String username, String userrole) {
        logger.info("Start get user list by username {} with role {}", username, userrole);

        List<UsersEntity> usersEntities = usersRepository.findByFilter(
                filter.getEmail(),
                filter.getUsername(),
                filter.getName(),
                filter.getSurname(),
                filter.getTaxcode(),
                filter.getRole());

        logger.info("Finish get user list by username {} with role {}", username, userrole);

        if(usersEntities != null && !usersEntities.isEmpty()){
            return usersEntities.stream()
                    .map(userEntity -> convertFromEntityToObject(userEntity, userrole))
                    .collect(Collectors.toList());
        }else
            return Collections.emptyList();
    }

    private User convertFromEntityToId(UsersEntity entity) {
        User user = new User();
        user.setId(entity.getIdUser());
        return user;
    }

    private User convertFromEntityToObject(UsersEntity entity, String userrole) {
        User user = new User();
        user.setId(entity.getIdUser());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setName(entity.getName());
        user.setSurname(entity.getSurname());
        if(userrole.equalsIgnoreCase("ADMIN"))
            user.setTaxcode(entity.getTaxCode());
        if (entity.getRoles() != null && !userrole.equalsIgnoreCase("USER")) {
            user.setRoles(entity.getRoles().stream()
                    .map(RolesEntity::getRoleType)
                    .map(RoleTypeEntity::getRoleName)
                    .collect(Collectors.toList()));
        }

        return user;
    }

    public boolean isFieldUserEmpty(User user){
        return StringUtils.isEmpty(user.getEmail()) ||
                StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getName()) ||
                StringUtils.isEmpty(user.getSurname()) ||
                StringUtils.isEmpty(user.getTaxcode());
    }

    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }
}
