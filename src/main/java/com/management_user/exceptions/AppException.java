package com.management_user.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException{

  private final HttpStatus httpStatus;
  public AppException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus=httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

//  public UserNotFoundException(String message) {
//    super(message);
//  }
}
