package com.ffsns.sns.service;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.Post;
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

    @Transactional
    public Post modify(String title, String body, String userName, Integer id){
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow
                (() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",id)));

        if(postEntity.getUserEntity() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName,id));
        }


        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postRepository.saveAndFlush(postEntity));

    }

}
