package com.ec2m.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="user_relations")
public class EntityUserRelations extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private EntityUser followerUser;

    @OneToOne
    private EntityUser followedUser;

    private Date acceptedDate;

    private Date blockedDate;

    private boolean closeFriend;

    private boolean blocked;

    private boolean accepted;

    private boolean deleted;

    private boolean waiting;

    public EntityUserRelations(EntityUser follower, EntityUser followed){
        this.followerUser = follower;
        this.followedUser = followed;
        this.accepted = false;
        this.blocked = false;
        this.deleted = false;
        this.closeFriend = false;
        this.waiting = true;
        this.setCreatedBy(follower.getUsername());
        this.setCreatedDate(new Date());
        this.setLastModifiedBy(follower.getUsername());
        this.setLastModifiedDate(new Date());
    }
}
