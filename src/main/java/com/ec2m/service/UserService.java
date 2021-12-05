package com.ec2m.service;

import com.ec2m.enums.EnumBaseServiceResult;
import com.ec2m.enums.EnumBaseServiceResult;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder;
    SimpleDateFormat dateFormatter;

    @EventListener(ApplicationReadyEvent.class)
    public void defineUtils() {
        encoder = new BCryptPasswordEncoder();
        dateFormatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public EnumBaseServiceResult SaveUser(SaveUserPayload userPayload){

        try{
            if(GetUser(userPayload.getUsername()) != null){
                return EnumBaseServiceResult.Failed;
            }else{
                EntityUser newUser = CreateUser(userPayload);
                if(newUser != null){
                    userRepository.save(newUser);
                    return EnumBaseServiceResult.Successful;
                }
                return EnumBaseServiceResult.InvalidData;
            }
        }catch(Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.InvalidData;
        }
    }

    public EnumBaseServiceResult Login(LoginPayload loginPayload){
        Optional<EntityUser> u = userRepository.findByEmailAndDeleted(loginPayload.getEmail(), false);
        if(u.isPresent()){
            EntityUser user = u.get();
            if(encoder.matches(loginPayload.getPassword(),user.getPassword())){
                return EnumBaseServiceResult.Successful;
            }else{
                return EnumBaseServiceResult.Failed;
            }
        }
        return EnumBaseServiceResult.Failed;
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
                user.setLastModifiedBy(username);
                user.setLastModifiedDate(new Date());
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
                if(userPayload.getUsername() != null){userToBeUpdated.setUsername(userPayload.getUsername()); userToBeUpdated.setLastModifiedBy(userPayload.getUsername());}
                if(userPayload.getEmail() != null){userToBeUpdated.setEmail(userPayload.getEmail());}
                if(userPayload.getName() != null){userToBeUpdated.setName(userPayload.getName());}
                if(userPayload.getLastname() != null){userToBeUpdated.setLastname(userPayload.getLastname());}
                if(userPayload.getCountry() != null){userToBeUpdated.setCountry(userPayload.getCountry());}
                if(userPayload.getCity() != null){userToBeUpdated.setCity(userPayload.getCity());}
                if(userPayload.getProfession() != null){userToBeUpdated.setProfession(userPayload.getProfession());}
                if(userPayload.getBio() != null){userToBeUpdated.setBio(userPayload.getBio());}
                if(userPayload.getBirthday() !=null){userToBeUpdated.setBirthday(dateFormatter.parse(userPayload.getBirthday()));}
                userToBeUpdated.setLastModifiedDate(new Date());
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
                    userToBeUpdated.setLastModifiedBy(username);
                    userToBeUpdated.setLastModifiedDate(new Date());
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
        try{
            String psw = (new BCryptPasswordEncoder().encode(userPayload.getPassword()));
            EntityUser u = new EntityUser(userPayload.getUsername(),
                    userPayload.getName(),userPayload.getLastname(),
                    psw, userPayload.getEmail(),
                    "", userPayload.getCountry(),
                    userPayload.getCity(),userPayload.getProfession(),
                    userPayload.getBio(), dateFormatter.parse(userPayload.getBirthday()));
            u.setActive(true);
            u.setDeleted(false);
            u.setStatus(true);
            return u;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
