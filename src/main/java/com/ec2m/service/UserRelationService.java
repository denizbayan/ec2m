package com.ec2m.service;

import com.ec2m.enums.EnumBaseServiceResult;
import com.ec2m.model.EntityUser;
import com.ec2m.model.EntityUserRelations;
import com.ec2m.payload.GetFollowRequestsPayload;
import com.ec2m.payload.MinimalUserPayload;
import com.ec2m.payload.SentFollowRequestPayload;
import com.ec2m.repository.UserRelationRepository;
import com.ec2m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserRelationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRelationRepository userRelationRepository;

    public List<MinimalUserPayload> GetFollowers(Long userId){
        boolean userCheck = ValidateSingleUser(userId);
        if(userCheck){
            return userRelationRepository.findFollowersByUserId(userId);
        }else{
            return null;
        }
    }

    public List<MinimalUserPayload> GetFollowedUsers(Long userId){
        boolean userCheck = ValidateSingleUser(userId);
        if(userCheck){
            return userRelationRepository.findFollowedUsersByUserId(userId);
        }else{
            return null;
        }
    }

    public EnumBaseServiceResult SendFollowRequest(SentFollowRequestPayload requestPayload){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users !=null){

                    EntityUserRelations relation = new EntityUserRelations(users.get(0),users.get(1));
                    userRelationRepository.save(relation);
                    return EnumBaseServiceResult.Successful;

            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public EnumBaseServiceResult DeleteFollowRequest(SentFollowRequestPayload requestPayload){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users !=null){

                    Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(users.get(0).getId(),users.get(1).getId(),false);
                    if(r.isPresent()){
                        EntityUserRelations relation = r.get();
                        relation.setDeleted(true);
                        userRelationRepository.save(relation);
                        return EnumBaseServiceResult.Successful;
                    }else{
                        return EnumBaseServiceResult.InvalidData;
                    }


            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public List<GetFollowRequestsPayload> GetFollowRequests(Long userId){
        boolean userCheck = ValidateSingleUser(userId);

        if(userCheck){
            return userRelationRepository.findWaitingFollowRequestsByReceiverUserId(userId);
        }else{
            return null;
        }
    }

    public List<GetFollowRequestsPayload> GetSentFollowRequests(Long userId){
        boolean userCheck = ValidateSingleUser(userId);
        if(userCheck){
            return userRelationRepository.findWaitingFollowRequestsBySenderUserId(userId);
        }else{
            return null;
        }
    }

    public EnumBaseServiceResult AcceptOrRejectFollowRequest(SentFollowRequestPayload requestPayload, boolean accepted){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users !=null){

                    Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(users.get(0).getId(),users.get(1).getId(),false);
                    if(r.isPresent()){
                        if(accepted){
                            EntityUserRelations relation = r.get();
                            relation.setAccepted(accepted);
                            relation.setAcceptedDate(new Date());
                            relation.setWaiting(false);
                            userRelationRepository.save(relation);
                        }else{
                            userRelationRepository.delete(r.get());
                        }
                        return EnumBaseServiceResult.Successful;
                    }else{
                        return EnumBaseServiceResult.InvalidData;
                    }


            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public EnumBaseServiceResult DeleteFollowerOrFollowedUser(SentFollowRequestPayload requestPayload, boolean deleteFollower){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);// follower user is index 0, followed user is index 1
            if(users !=null){

                    // change requestPayload parameters with each other, sender to receiver and vice versa so that sender user is now receiver user which means we now delete received follow relationship
                    if(deleteFollower){
                        Long temp = requestPayload.senderUserId;
                        requestPayload.senderUserId = requestPayload.receiverUserId;
                        requestPayload.receiverUserId = temp;
                    }
                    Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(requestPayload.senderUserId,requestPayload.receiverUserId,false);
                    if(r.isPresent()){
                        EntityUserRelations relation = r.get();
                        relation.setAccepted(false);
                        relation.setDeleted(true);
                        userRelationRepository.save(relation);
                        return EnumBaseServiceResult.Successful;
                    }else{
                        return EnumBaseServiceResult.InvalidData;
                    }

            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public EnumBaseServiceResult BlockUser(SentFollowRequestPayload requestPayload){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users != null){
                //if followedUser or follower user
                Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(users.get(0).getId(),users.get(1).getId(),false);
                if(r.isPresent()){
                    EntityUserRelations relation = r.get();
                    relation.setBlocked(true);
                    relation.setWaiting(false);
                    relation.setBlockedDate(new Date());
                    userRelationRepository.save(relation);

                }else{//no relation before
                    EntityUserRelations blockRelation = new EntityUserRelations(users.get(0),users.get(1));
                    blockRelation.setBlocked(true);
                    blockRelation.setWaiting(false);
                    blockRelation.setBlockedDate(blockRelation.getCreatedDate());
                    userRelationRepository.save(blockRelation);
                }
                return EnumBaseServiceResult.Successful;
            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public EnumBaseServiceResult RemoveBlockOfUser(SentFollowRequestPayload requestPayload){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users != null){
                Optional<EntityUserRelations> r = userRelationRepository.findBlockedRelation(requestPayload.senderUserId,requestPayload.receiverUserId,false);
                if(r.isPresent()){
                    userRelationRepository.delete(r.get());
                    return EnumBaseServiceResult.Successful;
                }else{
                    return EnumBaseServiceResult.InvalidData;
                }
            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e ){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public List<MinimalUserPayload> ListBlockedUsers(Long userId){
        try{
            boolean userCheck = ValidateSingleUser(userId);
            if(userCheck){
                return userRelationRepository.findBlockedUsers(userId);
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public EnumBaseServiceResult AddOrRemoveCloseFriend(SentFollowRequestPayload requestPayload, boolean closeFriend){
        try{
            ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
            if(users != null){
                Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(users.get(0).getId(),users.get(1).getId(),false);
                if(r.isPresent()){
                    EntityUserRelations relation = r.get();
                    relation.setCloseFriend(closeFriend);
                    userRelationRepository.save(relation);
                    return EnumBaseServiceResult.Successful;
                }else{
                    return EnumBaseServiceResult.InvalidData;
                }
            }else{
                return EnumBaseServiceResult.InvalidData;
            }
        }catch (Exception e){
            e.printStackTrace();
            return EnumBaseServiceResult.Failed;
        }
    }

    public List<MinimalUserPayload> ListCloseFriends(Long userId){
        try{
            boolean userCheck = ValidateSingleUser(userId);
            if(userCheck){
                return userRelationRepository.findCloseFriends(userId);
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /******************************HELPERS******************************/

    private ArrayList<EntityUser> ValidateRequestUsers(SentFollowRequestPayload requestPayload){
        try{
            Long followerId = requestPayload.senderUserId;
            Long receiverId = requestPayload.receiverUserId;
            if(followerId == null ||receiverId == null){
                return null;
            }
            Optional<EntityUser> fUser = userRepository.findByIdAndDeleted(followerId, false);
            Optional<EntityUser> rUser = userRepository.findByIdAndDeleted(receiverId, false);

            if(fUser.isPresent() && rUser.isPresent()){
                EntityUser followerUser = fUser.get();
                EntityUser receiverUser = rUser.get();
                ArrayList<EntityUser> users = new ArrayList<>();
                users.add(followerUser);
                users.add(receiverUser);
                return users;
            }else{
                return null;
            }
        }catch (Exception e ){
            e.printStackTrace();
            return null;
        }
    }

    private boolean ValidateSingleUser(Long userId){
        if(userId == null){
            System.out.println("user id null");
            return false;
        }else{
            Optional<EntityUser> u = userRepository.findByIdAndDeleted(userId,false);
            if(!u.isPresent()){
                System.out.println("user null");
                return false;
            }else{
                return true;
            }
        }
    }

}
