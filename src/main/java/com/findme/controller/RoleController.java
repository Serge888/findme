package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.models.Role;
import com.findme.models.RoleStatus;
import com.findme.models.User;
import com.findme.service.RoleService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RoleController {
    private RoleService roleService;
    private UserService userService;

    @Autowired
    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @GetMapping(path = "/save-role")
    public ResponseEntity saveRoles()
            throws BadRequestException, InternalServerException {

        Role user = new Role();
        user.setRoleStatus(RoleStatus.USER);
        roleService.save(user);

        Role admin = new Role();
        admin.setRoleStatus(RoleStatus.ADMIN);
        roleService.save(admin);

        Role superAdmin = new Role();
        superAdmin.setRoleStatus(RoleStatus.SUPER_ADMIN);
        roleService.save(superAdmin);

        return new ResponseEntity<>("Roles were saved", HttpStatus.OK);
    }


    @GetMapping(path = "/set-users-role")
    public ResponseEntity setUserRole()
            throws BadRequestException, InternalServerException {

        Role roleUser = roleService.findById(80L);
        Role roleAdmin = roleService.findById(81L);
        Role roleSuperAdmin = roleService.findById(82L);

        Set<Role> userSet = new HashSet<>();
        userSet.add(roleUser);

        Set<Role> adminSet = new HashSet<>();
        adminSet.add(roleUser);
        adminSet.add(roleAdmin);

        Set<Role> superAdminSet = new HashSet<>();
        superAdminSet.add(roleUser);
        superAdminSet.add(roleAdmin);
        superAdminSet.add(roleSuperAdmin);

        for (long i = 1; i < 80; i++) {
            User user = userService.findById(i);
            if (user != null) {
                if (user.getId() > 2) {
                    user.setRoles(userSet);
                    userService.update(user);
                } else if (user.getId() == 1) {
                    user.setRoles(superAdminSet);
                    userService.update(user);
                } else if (user.getId() == 2) {
                    user.setRoles(adminSet);
                    userService.update(user);
                }
            }
        }
        return new ResponseEntity<>("Roles were set.", HttpStatus.OK);
    }




}
