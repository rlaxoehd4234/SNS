package com.ffsns.sns.service;

import com.ffsns.sns.entity.UserEntity;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.fixture.UserEntityFixture;
import com.ffsns.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작하는_경우(){
        String userName = "kimtaedong";
        String password = "1111";

        //mocking
        //아직 회원가입이 되지 않은 경우에는 empty 값이 반환된다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));


        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void 회원가입시_이미_UserName_으로_가입한_사람이_존재하는_경우(){
        String userName = "kimtaedong";
        String password = "1111";
        UserEntity user = UserEntityFixture.get(userName,password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

        Assertions.assertThrows( SnsApplicationException.class, () -> userService.join(userName ,password));

    }

    @Test
    void 로그인이_정상적으로_동작하는_경우(){
        String userName = "kimtaedong";
        String password = "1111";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void 로그인시_아이디가_존재하는_경우(){
        String userName = "kimtaedong";
        String password = "1111";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows( SnsApplicationException.class, () -> userService.login(userName ,password));

    }
    @Test
    void 로그인시__password_가_틀린경우(){
        String userName = "kimtaedong";
        String password = "1111";
        String wrongPassword = "wrongPassword";
        UserEntity user = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        Assertions.assertThrows( SnsApplicationException.class, () -> userService.login(userName ,wrongPassword));

    }

}
