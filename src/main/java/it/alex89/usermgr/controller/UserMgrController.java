package it.alex89.usermgr.controller;

import it.alex89.usermgr.message.RabbitMessagePublisher;
import it.alex89.usermgr.model.User;
import it.alex89.usermgr.model.UserEdit;
import it.alex89.usermgr.model.UserFilter;
import it.alex89.usermgr.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserMgrController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private RabbitMessagePublisher rabbitMessagePublisher;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATOR')")
    @PostMapping("/add")
    public User newUser(HttpServletRequest request, @RequestBody User newUser, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        User user = usersService.insertUser(newUser, username, role);
        rabbitMessagePublisher.sendMessage("Added user: " + username);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATOR') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public User findUserById(HttpServletRequest request, @PathVariable String id, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        return usersService.getUserById(id, username, role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATOR') or hasRole('ROLE_USER')")
    @PostMapping("/usersByFilter")
    public List<User> userByFilter(HttpServletRequest request, @RequestBody UserFilter filter, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        return usersService.getUserByFilter(filter, username, role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATOR')")
    @PutMapping("/{id}")
    public User editUser(HttpServletRequest request, @RequestBody UserEdit editUser, @PathVariable String id, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        return usersService.editUser(id, editUser, username, role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPERATOR')")
    @PutMapping("/{id}/role")
    public User editRole(HttpServletRequest request, @RequestBody List<String> roles, @PathVariable String id, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        return usersService.editRole(id, roles, username, role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(HttpServletRequest request, @PathVariable String id, @AuthenticationPrincipal Jwt jwt) throws Exception {
        String username = getUsername(jwt);
        String role = getUserRole(request);
        usersService.deleteUser(id, username, role);
    }

    private String getUsername(Jwt jwt){
        if (jwt == null) {
            return "NA";
        }

        return jwt.getClaimAsString("preferred_username");
    }

    private String getUserRole(HttpServletRequest request){
        if (request == null) {
            return "NA";
        }
        if (request.isUserInRole("ADMIN")) {
            return "ADMIN";
        }
        if (request.isUserInRole("OPERATOR")) {
            return "OPERATOR";
        }
        if (request.isUserInRole("USER")) {
            return "USER";
        }

        return null;
    }
}
