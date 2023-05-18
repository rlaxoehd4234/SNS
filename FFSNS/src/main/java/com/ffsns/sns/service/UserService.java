package com.ffsns.sns.service;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.model.entity.UserEntity;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.User;
import com.ffsns.sns.repository.UserEntityRepository;
import com.ffsns.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;



    @Transactional
    public User join(String userName, String password ){

        userEntityRepository.findByUserName(userName).ifPresent( it ->  {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is Duplicated" , userName));
        });

        UserEntity user = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));


        return User.fromEntity(user);
    }

    public String login(String userName, String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found ", userName)));

        //비밀번호 체크
        if(!encoder.matches(password, userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성

        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }
}
