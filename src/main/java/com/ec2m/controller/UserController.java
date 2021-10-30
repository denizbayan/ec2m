package com.ec2m.controller;

import com.ec2m.enums.EnumSaveUserResult;
import com.ec2m.model.EntityUser;
import com.ec2m.payload.LoginPayload;
import com.ec2m.payload.SaveUserPayload;
import com.ec2m.repository.UserRepository;
import com.ec2m.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/getUser/{username}")
    public EntityUser GetUserByUsername(@PathVariable String username) {
        System.out.println("what up bitch");
        Optional<EntityUser> u = userRepository.findByUsernameAndDeleted(username,false);
        if(u.isPresent()){

            return u.get();
        }else{
            System.out.println(username +" yok");
            return null;
        }
    }


    @PostMapping("/signup")
    public Enum<EnumSaveUserResult> SaveUser(@RequestBody SaveUserPayload req) {
        return userService.saveUser(req);
    }
}
