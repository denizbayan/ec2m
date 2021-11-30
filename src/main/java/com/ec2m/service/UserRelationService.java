package com.ec2m.service;

import com.ec2m.enums.EnumAddOrUpdateFollowRequest;
import com.ec2m.model.EntityUser;
import com.ec2m.model.EntityUserRelations;
import com.ec2m.payload.GetFollowRequestsPayload;
import com.ec2m.payload.SentFollowRequestPayload;
import com.ec2m.repository.UserRelationRepository;
import com.ec2m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRelationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRelationRepository userRelationRepository;

    public EnumAddOrUpdateFollowRequest SendFollowRequest(SentFollowRequestPayload requestPayload){
        ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
        if(users !=null){
            try{
                EntityUserRelations relation = new EntityUserRelations(users.get(0),users.get(1));
                userRelationRepository.save(relation);
                return EnumAddOrUpdateFollowRequest.Successful;
            }catch (Exception e){
                e.printStackTrace();
                return EnumAddOrUpdateFollowRequest.Failed;
            }
        }else{
            return EnumAddOrUpdateFollowRequest.InvalidData;
        }
    }

    public EnumAddOrUpdateFollowRequest DeleteFollowRequest(SentFollowRequestPayload requestPayload){
        ArrayList<EntityUser> users = ValidateRequestUsers(requestPayload);
        if(users !=null){
            try{
                Optional<EntityUserRelations> r = userRelationRepository.findByFollowerAndFollowedAndDeleted(users.get(0).getId(),users.get(1).getId(),false);
                if(r.isPresent()){
                    EntityUserRelations relation = r.get();
                    relation.setDeleted(true);
                    userRelationRepository.save(relation);
                    return EnumAddOrUpdateFollowRequest.Successful;
                }else{
                    return EnumAddOrUpdateFollowRequest.InvalidData;
                }

            }catch (Exception e){
                e.printStackTrace();
                return EnumAddOrUpdateFollowRequest.Failed;
            }
        }else{
            return EnumAddOrUpdateFollowRequest.InvalidData;
        }

    }

    public List<GetFollowRequestsPayload> GetFollowRequests(Long userId){
        boolean userCheck = ValidateSingleUser(userId);

        if(userCheck){
            List<EntityUserRelations> followRequests = userRelationRepository.findWaitingFollowRequestsByReceiverUserId(userId);
            return ConvertRequestsToPayload(followRequests);

        }else{
            return null;
        }
    }

    public List<GetFollowRequestsPayload> GetSentFollowRequests(Long userId){
        boolean userCheck = ValidateSingleUser(userId);

        if(userCheck){
            List<EntityUserRelations> followRequests = userRelationRepository.findWaitingFollowRequestsBySenderUserId(userId);
            return ConvertRequestsToPayload(followRequests);
        }else{
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

    private List<GetFollowRequestsPayload> ConvertRequestsToPayload(List<EntityUserRelations> requests){
        if(requests.size()==0){
            System.out.println("request null");
            return null;
        }
        List<GetFollowRequestsPayload> payload = new ArrayList<>();
        for(EntityUserRelations request : requests){
            System.out.println(request.getFollowerUser().getUsername()+" send at "+request.getCreatedDate());
            payload.add(new GetFollowRequestsPayload(request.getFollowerUser(),request.getCreatedDate()));
        }
        return payload;
    }
}
