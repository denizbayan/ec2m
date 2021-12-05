package com.ec2m.controller;

import com.ec2m.enums.EnumBaseServiceResult;

import com.ec2m.enums.EnumUpdateUserResult;
import com.ec2m.model.EntityUser;
import com.ec2m.payload.ChangeUserPasswordPayload;
import com.ec2m.payload.LoginPayload;
import com.ec2m.payload.SaveUserPayload;
import com.ec2m.repository.UserRepository;
import com.ec2m.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/getUser")
    public EntityUser GetUserByUsername(@Validated @RequestBody String username) {return userService.GetUserByUsername(username);}

    @PostMapping("/deleteUser")
    public String DeleteUser(@Validated @RequestBody String username) {return userService.DeleteByUsername(username);}

    @PostMapping("/signup")
    public EnumBaseServiceResult SaveUser(@Validated @RequestBody SaveUserPayload req) {return userService.SaveUser(req);}

    @PostMapping("/login")
    public EnumBaseServiceResult Login(@Validated @RequestBody LoginPayload req) {return userService.Login(req);}

    @PostMapping("/updateUser")
    public EnumUpdateUserResult UpdateUser(@Validated @RequestBody SaveUserPayload req){return userService.UpdateUser(req);}

    @PostMapping("/updateUserPassword")
    public EnumUpdateUserResult UpdateUserPassword(@Validated @RequestBody ChangeUserPasswordPayload req){return userService.UpdateUserPassword(req);}
}
