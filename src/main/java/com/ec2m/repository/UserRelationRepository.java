package com.ec2m.repository;


import com.ec2m.model.EntityUserRelations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRelationRepository extends JpaRepository<EntityUserRelations,Long> {

    @Query("select ur from EntityUserRelations ur where ur.followerUser.id =?1 and ur.followedUser.id =?2 and ur.deleted=?3")
    Optional<EntityUserRelations> findByFollowerAndFollowedAndDeleted(Long followerId, Long followedId, boolean deleted);

    @Query("select ur from EntityUserRelations ur where ur.followedUser.id =?1 and ur.accepted = false and ur.deleted = false")
    List<EntityUserRelations> findWaitingFollowRequestsByReceiverUserId(Long userId);

    @Query("select ur from EntityUserRelations ur where ur.followerUser.id =?1 and ur.accepted = false and ur.deleted = false")
    List<EntityUserRelations> findWaitingFollowRequestsBySenderUserId(Long userId);

}
