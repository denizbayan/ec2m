package com.ec2m.controller;


import com.ec2m.enums.EnumAddOrUpdateFollowRequest;
import com.ec2m.model.EntityUser;
import com.ec2m.model.EntityUserRelations;
import com.ec2m.payload.GetFollowRequestsPayload;
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
    //getFollowedUsers

    //sendFollowRequest
    @PostMapping("/sendFollowRequest")
    public EnumAddOrUpdateFollowRequest SendFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.SendFollowRequest(req);}

    //deleteFollowRequest
    @PostMapping("/deleteFollowRequest")
    public EnumAddOrUpdateFollowRequest DeleteFollowRequest(@Validated @RequestBody SentFollowRequestPayload req) {return userRelationService.DeleteFollowRequest(req);}

    //getReceivedFollowRequests
    @PostMapping("/getReceivedFollowRequests")
    public List<GetFollowRequestsPayload> GetReceivedFollowRequests(@Validated @RequestBody String userId) {return userRelationService.GetFollowRequests(Long.parseLong(userId));}

    //getSentFollowRequests
    @PostMapping("/getSentFollowRequests")
    public List<GetFollowRequestsPayload> GetSentFollowRequests(@Validated @RequestBody String userId) {return userRelationService.GetSentFollowRequests(Long.parseLong(userId));}

    //acceptFollowRequest
    //rejectFollowRequest

    //deleteFollower
    //deleteFollowedUser
    //addToBlockedUsers
    //deleteFromBlockedUsers
    //addToCloseFriends
    //deleteFromCloseFriends

    //checkUserRelationStatus
}
