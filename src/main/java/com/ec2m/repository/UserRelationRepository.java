package com.ec2m.repository;


import com.ec2m.model.EntityUserRelations;
import com.ec2m.payload.GetFollowRequestsPayload;
import com.ec2m.payload.MinimalUserPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRelationRepository extends JpaRepository<EntityUserRelations,Long> {

    @Query("select ur from EntityUserRelations ur where ur.followerUser.id =?1 and ur.followedUser.id =?2 and ur.deleted=?3")
    Optional<EntityUserRelations> findByFollowerAndFollowedAndDeleted(Long followerId, Long followedId, boolean deleted);

    @Query("select new com.ec2m.payload.GetFollowRequestsPayload(ur.followerUser.id as userId, ur.followerUser.username as username, ur.createdDate as sendDate) from EntityUserRelations ur where ur.followedUser.id =?1 and ur.waiting = true and ur.deleted = false")
    List<GetFollowRequestsPayload> findWaitingFollowRequestsByReceiverUserId(Long userId);

    @Query("select new com.ec2m.payload.GetFollowRequestsPayload(ur.followedUser.id as userId, ur.followedUser.username as username, ur.createdDate as sendDate) from EntityUserRelations ur where ur.followerUser.id =?1 and ur.waiting = true and ur.deleted = false")
    List<GetFollowRequestsPayload> findWaitingFollowRequestsBySenderUserId(Long userId);

    @Query("select new com.ec2m.payload.MinimalUserPayload(ur.followerUser.id as userId, ur.followerUser.username as username) from EntityUserRelations ur where ur.followedUser.id=?1 and ur.accepted = true and ur.deleted = false")
    List<MinimalUserPayload>findFollowersByUserId(Long userId);

    @Query("select new com.ec2m.payload.MinimalUserPayload(ur.followedUser.id as userId, ur.followedUser.username as username) from EntityUserRelations ur where ur.followerUser.id=?1 and ur.accepted = true and ur.deleted = false")
    List<MinimalUserPayload>findFollowedUsersByUserId(Long userId);

    @Query("select ur from EntityUserRelations ur where ur.followedUser.id =?1 and ur.followedUser.id =?2 and ur.deleted =?3 and ur.blocked = true")
    Optional<EntityUserRelations>findBlockedRelation(Long senderId, Long receiverId, boolean deleted);

    @Query("select new com.ec2m.payload.MinimalUserPayload(ur.followedUser.id as userId, ur.followedUser.username as username) from EntityUserRelations ur where ur.blocked=true and ur.deleted = false")
    List<MinimalUserPayload> findBlockedUsers(Long userId);

    @Query("select new com.ec2m.payload.MinimalUserPayload(ur.followedUser.id as userId, ur.followedUser.username as username) from EntityUserRelations ur where ur.closeFriend=true and ur.deleted = false")
    List<MinimalUserPayload> findCloseFriends(Long userId);


}
