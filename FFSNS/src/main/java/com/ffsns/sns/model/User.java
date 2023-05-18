package com.ffsns.sns.model;

import com.ffsns.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class User {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity user){
        return new User(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getRole(),
                user.getRegisteredAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }

}
