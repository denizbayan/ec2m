package com.ec2m.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordPayload {
    public String username;
    public String oldPassword;
    public String newPassword;
}
