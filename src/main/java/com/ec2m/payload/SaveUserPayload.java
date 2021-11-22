package com.ec2m.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUserPayload {

    private String username;
    private String password;
    private String email;
    private String name;
    private String lastname;
    private String country;
    private String city;
    private String profession;
    private String bio;

}
