package com.ffsns.sns.fixture;

import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import org.springframework.security.core.parameters.P;

public class PostEntityFixture {

    public static PostEntity get(String userName, Integer id){
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUserEntity(user);
        result.setId(id);


        return result;
    }
}
