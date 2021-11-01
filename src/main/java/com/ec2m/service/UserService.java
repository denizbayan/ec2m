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
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public EnumSaveUserResult SaveUser(SaveUserPayload userPayload){

        try{
            if(userExists(userPayload.getUsername())){
                return EnumSaveUserResult.UserExists;
            }else{
                EntityUser newUser = CreateUser(userPayload);
                userRepository.save(newUser);
                return EnumSaveUserResult.Successful;
            }
        }catch(Exception e){
            e.printStackTrace();
            return EnumSaveUserResult.InvalidData;
        }
    }

    public EnumLoginResult Login(LoginPayload loginPayload){
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

    public EntityUser GetUserByUsername(String username){
        try{
            Optional<EntityUser> u = userRepository.findByUsernameAndDeleted(username,false);
            if(u.isPresent()){
                return u.get();
            }else{
                System.out.println(username +" yok");
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String DeleteByUsername(String username){
        try{
            Optional<EntityUser> u = userRepository.findByUsernameAndDeleted(username,false);
            if(u.isPresent()){
                EntityUser user = u.get();
                user.setDeleted(true);
                user.setUsernameBeforeDeleted(user.getUsername());
                user.setUsername(user.getUsername()+ UUID.randomUUID());
                userRepository.save(user);
                return "Successful";
            }else{
                return "User Does not exist.";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "Failed." + e.toString();
        }
    }

    /******************************HELPERS******************************/
    private boolean userExists(String username){
        Optional<EntityUser> u = userRepository.findByUsernameAndDeleted(username,false);
        return u.isPresent();
    }

    private EntityUser CreateUser(SaveUserPayload userPayload){
        EntityUser u = new EntityUser(userPayload.getUsername(),
                userPayload.getName(),userPayload.getLastname(),
                userPayload.getPassword(), userPayload.getEmail(),
                "", userPayload.getCountry(),
                userPayload.getCity(),userPayload.getProfession());
        u.setActive(true);
        u.setDeleted(false);
        u.setStatus(true);
        return u;
    }

}
