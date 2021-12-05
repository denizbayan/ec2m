package com.ec2m.controller;


import com.ec2m.enums.EnumBaseServiceResult;
import com.ec2m.model.EntityUser;
import com.ec2m.model.EntityUserRelations;
import com.ec2m.payload.GetFollowRequestsPayload;
import com.ec2m.payload.MinimalUserPayload;
import com.ec2m.payload.SentFollowRequestPayload;
import com.ec2m.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/userRelation")
public class UserRelationController {

    @Autowired
    UserRelationService userRelationService;

    //getFollowers
    @PostMapping("/getFollowers")
    public List<MinimalUserPayload> GetFollowers(@Validated @RequestBody String userId) {return userRelationService.GetFollowers(Long.parseLong(userId));}

    //getFollowedUsers
    @PostMapping("/getFollowedUsers")
    public List<MinimalUserPayload> GetFollowedUsers(@Validated @RequestBody String userId) {return userRelationService.GetFollowedUsers(Long.parseLong(userId));}

    //sendFollowRequest
    @PostMapping("/sendFollowRequest")
    public EnumBaseServiceResult SendFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.SendFollowRequest(req);}

    //deleteFollowRequest
    @PostMapping("/deleteFollowRequest")
    public EnumBaseServiceResult DeleteFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.DeleteFollowRequest(req);}

    //getReceivedFollowRequests
    @PostMapping("/getReceivedFollowRequests")
    public List<GetFollowRequestsPayload> GetReceivedFollowRequests(@Validated @RequestBody String userId) {return userRelationService.GetFollowRequests(Long.parseLong(userId));}

    //getSentFollowRequests
    @PostMapping("/getSentFollowRequests")
    public List<GetFollowRequestsPayload> GetSentFollowRequests(@Validated @RequestBody String userId) {return userRelationService.GetSentFollowRequests(Long.parseLong(userId));}

    //acceptFollowRequest
    @PostMapping("/acceptFollowRequest")
    public EnumBaseServiceResult AcceptFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.AcceptOrRejectFollowRequest(req,true);}

    //rejectFollowRequest
    @PostMapping("/rejectFollowRequest")
    public EnumBaseServiceResult RejectFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.AcceptOrRejectFollowRequest(req,false);}

    //deleteFollower
    @PostMapping("/deleteFollower")
    public EnumBaseServiceResult DeleteFollower(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.DeleteFollowerOrFollowedUser(req,true);}

    //deleteFollowedUser
    @PostMapping("/deleteFollowedUser")
    public EnumBaseServiceResult DeleteFollowedUser(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.DeleteFollowerOrFollowedUser(req, false);}

    //addToBlockedUsers
    @PostMapping("/blockUser")
    public EnumBaseServiceResult BlockUser(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.BlockUser(req);}

    //deleteFromBlockedUsers
    @PostMapping("/removeBlockOfUser")
    public EnumBaseServiceResult RemoveBlockOfUser(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.RemoveBlockOfUser(req);}

    //listBlockedUsers
    @PostMapping("/listBlockedUsers")
    public List<MinimalUserPayload> ListBlockedUser(@Validated @RequestBody String userId) {return userRelationService.ListBlockedUsers(Long.parseLong(userId));}

    //addToCloseFriends
    @PostMapping("/addCloseFriend")
    public EnumBaseServiceResult AddOrRemoveCloseFriend(@Validated @RequestBody SentFollowRequestPayload req){return userRelationService.AddOrRemoveCloseFriend(req,true);}

    //deleteFromCloseFriends
    @PostMapping("/removeCloseFriend")
    public EnumBaseServiceResult RemoveCloseFriend(@Validated @RequestBody SentFollowRequestPayload req){return userRelationService.AddOrRemoveCloseFriend(req,false);}

    //getCloseFriends
    @PostMapping("/listCloseFriends")
    public List<MinimalUserPayload> ListCloseFriends(@Validated @RequestBody String userId) {return userRelationService.ListCloseFriends(Long.parseLong(userId));}
}
