package com.ec2m.payload;

import com.ec2m.model.EntityUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class GetFollowRequestsPayload {

    public EntityUser senderUser;
    public Date sendDate;


}
