package com.ec2m.controller;




import com.ec2m.payload.LoginPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.xml.validation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthorizationController {

    @PostMapping("/signin")
    public void authenticateUser(@Validated @RequestBody LoginPayload loginRequest) {
        System.out.println(loginRequest.getUsername() + " " + loginRequest.getPassword());
    }


}
