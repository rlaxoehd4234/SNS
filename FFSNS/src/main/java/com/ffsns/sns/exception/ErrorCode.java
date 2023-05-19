package com.ffsns.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid Password"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is Invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post Not Founded"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission Is Invalid");

    private HttpStatus status;
    private String message;

}
