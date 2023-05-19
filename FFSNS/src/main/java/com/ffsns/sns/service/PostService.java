package com.ffsns.sns.service;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import com.ffsns.sns.repository.PostEntityRepository;
import com.ffsns.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostEntityRepository postRepository;
    private final UserEntityRepository userRepository;

    @Transactional
    public void create(String title, String body, String userName){
        // Find user
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow
                (() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));


        postRepository.save(PostEntity.of(title, body, userEntity));

    }

    public void modify(String title, String body, String userName, Integer id){
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow
                (() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));


    }

}
