package com.ec2m.controller;




import com.ec2m.enums.EnumLoginResult;
import com.ec2m.model.EntityUser;
import com.ec2m.payload.LoginPayload;
import com.ec2m.repository.UserRepository;
import com.ec2m.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthorizationController {

}
