package com.ec2m.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})

public class EntityUser extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=255)
    private String username;

    @Size(max=255)
    private String usernameBeforeDeleted;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String profilePicturePath;

    private String country;

    private String city;

    private String profession;

    private String bio;

    private Date birthday;

    private boolean status;

    private boolean deleted;

    private boolean active;

    public EntityUser(String username, String name, String lastname, String password, String email, String profilePicturePath, String country, String city, String profession, String bio,Date birthday) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.country = country;
        this.city = city;
        this.profession = profession;
        this.bio = bio;
        this.birthday = birthday;
        this.setCreatedDate(new Date());
        this.setCreatedBy(username);
        this.setLastModifiedDate(new Date());
        this.setLastModifiedBy(username);
    }
}
