package com.hrs.booking.controller;

import com.hrs.booking.service.UserService;
import com.hrs.booking.view.user.RoleRequest;
import com.hrs.booking.view.user.RoleResponse;
import com.hrs.booking.view.user.UserRequest;
import com.hrs.booking.view.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.hrs.booking.auth.HRSAuthorisations.Privileges.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    @Secured(SELF_READ)
    public UserResponse getCurrentUser() {
        return new UserResponse(userService.getCurrentUser());
    }

    @GetMapping("{userId}")
    @Secured(USER_READ)
    public UserResponse getUser(@PathVariable(name = "userId") String userId) {
        return new UserResponse(userService.getUserDetail(userId));
    }

    @PostMapping
    @Secured(USER_WRITE)
    public UserResponse createUser(@RequestBody UserRequest request) {
        return new UserResponse(userService.createUser(request));
    }

    @PutMapping("update/password")
    @Secured(USER_WRITE)
    public void updatePassword(@RequestBody UserRequest request) {
        userService.updatePassword(request);
    }

    @PutMapping
    @Secured(USER_WRITE)
    public UserResponse updateUser(@RequestBody UserRequest request) {
        return new UserResponse(userService.updateUser(request));
    }

    // Role Part Below. (All above APIs for USER)

    @GetMapping("/role/{roleId}")
    @Secured(ROLE_READ)
    public RoleResponse getRole(@PathVariable String roleId) {
        return new RoleResponse(userService.getRole(roleId));
    }

    @PostMapping("/role")
    @Secured(ROLE_WRITE)
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return new RoleResponse(userService.addRole(request));
    }

    @PutMapping("/role")
    @Secured(ROLE_WRITE)
    public RoleResponse updateRole(@RequestBody RoleRequest request) {
        return new RoleResponse(userService.updateRole(request));
    }

}
