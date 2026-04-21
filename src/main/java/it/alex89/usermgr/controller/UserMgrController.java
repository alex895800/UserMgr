package it.alex89.usermgr.controller;

import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserEdit;
import it.alex89.usermgr.model.UserFilter;
import it.alex89.usermgr.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserMgrController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    private User newUser(@RequestBody User newUser) throws Exception {
        return usersService.insertUser(newUser);
    }

    @GetMapping("/{id}")
    private User findUserById(@PathVariable String id) throws Exception {
        return usersService.getUserById(id);
    }

    @PostMapping("/usersByFilter")
    private List<User> userByFilter(@RequestBody UserFilter filter) throws Exception {
        return usersService.getUserByFilter(filter);
    }

    @PutMapping("/{id}")
    private User editUser(@RequestBody UserEdit editUser, @PathVariable String id) throws Exception {
        return usersService.editUser(id, editUser);
    }

    @PutMapping("/{id}/role")
    private User editRole(@RequestBody List<String> roles, @PathVariable String id) throws Exception {
        return usersService.editRole(id, roles);
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable String id) throws Exception {
        usersService.deleteUser(id);
    }
}
