package com.ec2m.service;

import com.ec2m.enums.EnumLoginResult;
import com.ec2m.enums.EnumSaveUserResult;
import com.ec2m.enums.EnumUpdateUserResult;
import com.ec2m.model.EntityUser;
import com.ec2m.payload.ChangeUserPasswordPayload;
import com.ec2m.payload.LoginPayload;
import com.ec2m.payload.SaveUserPayload;
import com.ec2m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.*;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder;

    @EventListener(ApplicationReadyEvent.class)
    public void defineEncoder() {
        encoder = new BCryptPasswordEncoder();
    }

    public EnumSaveUserResult SaveUser(SaveUserPayload userPayload){

        try{
            if(GetUser(userPayload.getUsername()) != null){
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
            if(encoder.matches(loginPayload.getPassword(),user.getPassword())){
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

    public EnumUpdateUserResult UpdateUser(SaveUserPayload userPayload){

        try{
            if(GetUser(userPayload.getUsername()) == null){
                return EnumUpdateUserResult.UserNotFound;
            }else{
                EntityUser userToBeUpdated = GetUser(userPayload.getUsername());
                if(userPayload.getUsername() != null){userToBeUpdated.setUsername(userPayload.getUsername());}
                if(userPayload.getEmail() != null){userToBeUpdated.setEmail(userPayload.getEmail());}
                if(userPayload.getName() != null){userToBeUpdated.setName(userPayload.getName());}
                if(userPayload.getLastname() != null){userToBeUpdated.setLastname(userPayload.getLastname());}
                if(userPayload.getCountry() != null){userToBeUpdated.setCountry(userPayload.getCountry());}
                if(userPayload.getCity() != null){userToBeUpdated.setCity(userPayload.getCity());}
                if(userPayload.getProfession() != null){userToBeUpdated.setProfession(userPayload.getProfession());}
                if(userPayload.getBio() != null){userToBeUpdated.setBio(userPayload.getBio());}
                userRepository.save(userToBeUpdated);
                return EnumUpdateUserResult.Successful;
            }
        }catch(Exception e){
            e.printStackTrace();
            return EnumUpdateUserResult.InvalidData;
        }
    }

    public EnumUpdateUserResult UpdateUserPassword(ChangeUserPasswordPayload passwordPayload){
        String username = passwordPayload.getUsername();
        String oldPassword = passwordPayload.getOldPassword();
        String newPassword = passwordPayload.getNewPassword();
        try{
            if(GetUser(username) == null){
                return EnumUpdateUserResult.UserNotFound;
            }else{
                EntityUser userToBeUpdated = GetUser(username);
                if(encoder.matches(oldPassword,userToBeUpdated.getPassword())){
                    userToBeUpdated.setPassword(encoder.encode(newPassword));
                    userRepository.save(userToBeUpdated);
                    return EnumUpdateUserResult.Successful;
                }else{
                    return EnumUpdateUserResult.Failed;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return EnumUpdateUserResult.InvalidData;
        }

    }

    /******************************HELPERS******************************/
    private EntityUser GetUser(String username){
        Optional<EntityUser> u = userRepository.findByUsernameAndDeleted(username,false);
        return u.isPresent()?u.get():null;
    }

    private EntityUser CreateUser(SaveUserPayload userPayload){
        String psw = (new BCryptPasswordEncoder().encode(userPayload.getPassword()));
        EntityUser u = new EntityUser(userPayload.getUsername(),
                userPayload.getName(),userPayload.getLastname(),
                psw, userPayload.getEmail(),
                "", userPayload.getCountry(),
                userPayload.getCity(),userPayload.getProfession(),
                userPayload.getBio());
        u.setActive(true);
        u.setDeleted(false);
        u.setStatus(true);
        return u;
    }

}
