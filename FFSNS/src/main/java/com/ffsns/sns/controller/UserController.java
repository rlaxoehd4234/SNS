package com.ffsns.sns.controller;

import com.ffsns.sns.controller.request.UserJoinRequest;
import com.ffsns.sns.controller.request.UserLoginRequest;
import com.ffsns.sns.controller.response.AlarmResponse;
import com.ffsns.sns.controller.response.Response;
import com.ffsns.sns.controller.response.UserJoinResponse;
import com.ffsns.sns.controller.response.UserLoginResponse;
import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.Alarm;
import com.ffsns.sns.model.User;
import com.ffsns.sns.model.entity.UserEntity;
import com.ffsns.sns.service.UserService;
import com.ffsns.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        User user = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getUserName(),request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }


    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR));
        return Response.success(userService.alarmList(user.getId(),pageable).map(AlarmResponse::fromAlarm));
    }

}
