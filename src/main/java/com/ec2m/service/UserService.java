package com.ec2m.service;

import com.ec2m.enums.EnumLoginResult;
import com.ec2m.enums.EnumSaveUserResult;
import com.ec2m.model.EntityUser;
import com.ec2m.payload.LoginPayload;
import com.ec2m.payload.SaveUserPayload;
import com.ec2m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public EnumSaveUserResult saveUser(SaveUserPayload userPayload){

        EntityUser newUser = new EntityUser(userPayload.getUsername(),
                            userPayload.getName(),userPayload.getLastname(),
                            userPayload.getPassword(), userPayload.getEmail(),
                            "", userPayload.getCountry(),
                            userPayload.getCity(),userPayload.getProfession());
        System.out.println(userPayload.getUsername()+" "+
                userPayload.getName()+" "+userPayload.getLastname()+" "+
                userPayload.getPassword()+" "+ userPayload.getEmail()+" "+
                "profession"+" "+ userPayload.getCountry()+" "+
                userPayload.getCity()+" "+userPayload.getProfession());

        newUser.setActive(true);
        newUser.setDeleted(false);
        newUser.setStatus(true);
        userRepository.save(newUser);

        return EnumSaveUserResult.Successful;
    }

    public EnumLoginResult login(LoginPayload loginPayload){
        Optional<EntityUser> u = userRepository.findByEmailAndDeleted(loginPayload.getEmail(), false);

        if(u.isPresent()){
            EntityUser user = u.get();
            if(user.getPassword().equals(loginPayload.getPassword())){
                return EnumLoginResult.Successful;
            }else{
                return EnumLoginResult.Failed;
            }
        }
        return EnumLoginResult.Failed;

    }


}
