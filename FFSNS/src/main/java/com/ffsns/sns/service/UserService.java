package com.ffsns.sns.service;

import com.ffsns.sns.entity.UserEntity;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.User;
import com.ffsns.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password){

        userEntityRepository.findByUserName(userName).ifPresent( it ->  {
            throw new SnsApplicationException();
        });

        userEntityRepository.save(new UserEntity());


        return new User();
    }

    public String login(String userName, String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException());

        //비밀번호 체크
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException();
        }

        //토큰 생성



        return "";
    }
}
