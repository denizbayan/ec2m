package com.ec2m.payload;

import com.ec2m.model.EntityUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class GetFollowRequestsPayload {

    public Long userId;
    public String username;
    public Date sendDate;


}
