package com.ffsns.sns.controller.response;

import com.ffsns.sns.model.User;
import com.ffsns.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Integer id;
    private String userName;

    private UserRole userRole;

    public static UserJoinResponse fromUser(User user){

        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
