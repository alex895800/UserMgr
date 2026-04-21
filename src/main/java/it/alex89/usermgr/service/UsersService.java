package it.alex89.usermgr.service;

import it.alex89.usermgr.entity.RolesEntity;
import it.alex89.usermgr.entity.UsersEntity;
import it.alex89.usermgr.excp.AlreadyCreatedException;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserFilter;
import it.alex89.usermgr.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public User insertUser(User user) throws Exception {
        UsersEntity userLoaded = usersRepository.findById(user.getEmail())
                .orElse(null);
        if(userLoaded == null){
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
                    RolesEntity newRole = new RolesEntity();
                    newRole.setIdRoles(UUID.randomUUID().toString());
                    newRole.setRoleName(roleName);
                    usersEntity.addRole(newRole);
                }
            }

            UsersEntity usersSaved = usersRepository.save(usersEntity);

            return convertFromEntityToObject(usersSaved);
        }else{
            throw new AlreadyCreatedException("User with email: " + user.getEmail() + " already inserted");
        }
    }

    public User editUser(String email, User user) throws Exception {
        UsersEntity userLoaded = usersRepository.findById(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        userLoaded.setUsername(user.getUsername());
        userLoaded.setName(user.getName());
        userLoaded.setSurname(user.getSurname());
        userLoaded.setTaxCode(user.getTaxcode());
        userLoaded.setDateLastUpdate(new Date());
        userLoaded.setUserIdLastUpdate("SYSTEM");

        UsersEntity usersSaved = usersRepository.save(userLoaded);

        return convertFromEntityToObject(usersSaved);
    }

    public User editRole(String email, List<String> roles) throws Exception {
        UsersEntity userLoaded = usersRepository.findById(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        UsersEntity usersSaved = usersRepository.save(userLoaded);

        return convertFromEntityToObject(usersSaved);
    }

    public void deleteUser(String email) throws Exception {
        UsersEntity userLoaded = usersRepository.findById(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        usersRepository.delete(userLoaded);
    }

    public User getUserById(String id) throws Exception {
        return usersRepository.findById(id)
                .map(this::convertFromEntityToObject)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + id));
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
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setName(entity.getName());
        user.setSurname(entity.getSurname());
        user.setTaxcode(entity.getTaxCode());
        if (entity.getRoles() != null) {
            user.setRoles(entity.getRoles().stream()
                    .map(RolesEntity::getRoleName)
                    .collect(Collectors.toList()));
        }

        return user;
    }
}
