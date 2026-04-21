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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private RolesTypeRepository rolesTypeRepository;

    @Transactional
    public User insertUser(User user) throws Exception {
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
                        null);

        if(users == null || users.isEmpty()){
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setEmail(user.getEmail());
            usersEntity.setUsername(user.getUsername());
            usersEntity.setName(user.getName());
            usersEntity.setSurname(user.getSurname());
            usersEntity.setTaxCode(user.getTaxcode());
            usersEntity.setDateLastUpdate(new Date());
            usersEntity.setUserIdLastUpdate("SYSTEM");

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
            return convertFromEntityToObject(usersSaved);
        }else{
            throw new AlreadyCreatedException("User with email: " + user.getEmail() + " already inserted");
        }
    }

    @Transactional
    public User editUser(String id, UserEdit user) throws Exception {
        if(isFieldEditUserEmpty(user)){
            throw new ValidationException("The all or part of fields are empty");
        }

        UsersEntity usersEntity = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        usersEntity.setUsername(user.getUsername());
        usersEntity.setName(user.getName());
        usersEntity.setSurname(user.getSurname());
        usersEntity.setTaxCode(user.getTaxcode());
        usersEntity.setDateLastUpdate(new Date());
        usersEntity.setUserIdLastUpdate("SYSTEM");

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            usersEntity.getRoles().clear();
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
        return convertFromEntityToObject(usersSaved);
    }

    @Transactional
    public User editRole(String id, List<String> roles) throws Exception {
        UsersEntity usersEntity = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (roles != null && !roles.isEmpty()) {
            usersEntity.getRoles().clear();
            for (String roleName : roles) {
                RoleTypeEntity role = rolesTypeRepository.findById(roleName)
                        .orElseThrow(() -> new ValidationException("User with email: " + usersEntity.getEmail()
                                + " role name " + roleName + " not valid"));

                RolesEntity newRole = new RolesEntity();
                newRole.setIdRoles(UUID.randomUUID().toString());
                newRole.setRoleType(role);
                usersEntity.addRole(newRole);
            }
        }

        UsersEntity usersSaved = usersRepository.save(usersEntity);
        return convertFromEntityToObject(usersSaved);
    }

    public void deleteUser(String id) throws Exception {
        UsersEntity userLoaded = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        usersRepository.delete(userLoaded);
    }

    public User getUserById(String id) throws Exception {
        return usersRepository.findById(id)
                .map(this::convertFromEntityToObject)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public List<User> getUserByFilter(UserFilter filter) throws Exception {
        List<UsersEntity> usersEntities = usersRepository.findByFilter(
                filter.getEmail(),
                filter.getUsername(),
                filter.getName(),
                filter.getSurname(),
                filter.getTaxcode());

        if(usersEntities != null && !usersEntities.isEmpty()){
            return usersEntities.stream()
                    .map(this::convertFromEntityToObject)
                    .collect(Collectors.toList());
        }else
            return Collections.emptyList();
    }

    private User convertFromEntityToObject(UsersEntity entity) {
        User user = new User();
        user.setId(entity.getIdUser());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setName(entity.getName());
        user.setSurname(entity.getSurname());
        user.setTaxcode(entity.getTaxCode());
        if (entity.getRoles() != null) {
            user.setRoles(entity.getRoles().stream()
                    .map(RolesEntity::getRoleType)
                    .map(RoleTypeEntity::getRoleName)
                    .collect(Collectors.toList()));
        }

        return user;
    }

    public boolean isFieldEditUserEmpty(UserEdit user){
        return StringUtils.isEmpty(user.getUsername()) &&
                StringUtils.isEmpty(user.getName()) &&
                StringUtils.isEmpty(user.getSurname()) &&
                StringUtils.isEmpty(user.getTaxcode());
    }

    public boolean isFieldUserEmpty(User user){
        return StringUtils.isEmpty(user.getEmail()) &&
                StringUtils.isEmpty(user.getUsername()) &&
                StringUtils.isEmpty(user.getName()) &&
                StringUtils.isEmpty(user.getSurname()) &&
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
