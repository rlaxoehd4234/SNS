package com.ffsns.sns.service;

import com.ffsns.sns.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    public User join(){
        return new User();
    }

    public String login(){
        return "";
    }
}
