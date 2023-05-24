package com.ffsns.sns.service;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.AlarmArgs;
import com.ffsns.sns.model.AlarmType;
import com.ffsns.sns.model.Comment;
import com.ffsns.sns.model.Post;
import com.ffsns.sns.model.entity.*;
import com.ffsns.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class PostService {


    private final PostEntityRepository postRepository;
    private final UserEntityRepository userRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){
        // Find user
        UserEntity userEntity = validateUserEntity(userName);


        postRepository.save(PostEntity.of(title, body, userEntity));

    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer id){
        UserEntity userEntity = validateUserEntity(userName);
        PostEntity postEntity = validatePostEntity(id);

        if(postEntity.getUserEntity() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName,id));
        }


        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postRepository.saveAndFlush(postEntity));

    }


    @Transactional
    public void delete(String userName, Integer postId){
        UserEntity userEntity = validateUserEntity(userName);
        PostEntity postEntity = validatePostEntity(postId);
        if(postEntity.getUserEntity() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName,postId));
        }

        postRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){

        return postRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable){
        UserEntity userEntity = validateUserEntity(userName);


        return postRepository.findAllByUserEntity(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName){
        PostEntity postEntity = validatePostEntity(postId);
        UserEntity userEntity = validateUserEntity(userName);

        // TODO : 중복방지 확인을 위한 로직 추가
        likeEntityRepository.findByUserEntityAndPostEntity(userEntity, postEntity).ifPresent( it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKE, String.format("%s already post %d",userName,postId));
        });


        likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUserEntity(), AlarmType.NEW_COMMENT_ON_POST,new AlarmArgs(userEntity.getId(), postEntity.getId())));

    }

    public int likeCount(Integer postId){
        PostEntity postEntity = validatePostEntity(postId);

        return likeEntityRepository.countByPostEntity(postEntity);

    }


    @Transactional
    public void comment(Integer postId, String userName, String comment){
        PostEntity postEntity = validatePostEntity(postId);
        UserEntity userEntity = validateUserEntity(userName);

        // comment save
        commentEntityRepository.save(CommentEntity.of(postEntity,userEntity,comment));
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUserEntity(), AlarmType.NEW_COMMENT_ON_POST,new AlarmArgs(userEntity.getId(), postEntity.getId())));


    }
    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity post = validatePostEntity(postId);
        return commentEntityRepository.findAllByPostEntity(post, pageable).map(Comment::formEntity);
    }


    private PostEntity validatePostEntity(Integer postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%d not founded",postId)));
    }

    private UserEntity validateUserEntity(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }




}

