package com.management_user.base;

import com.management_user.constants.StatusCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponse<T> {
  private  T data;
  private String notification = StatusCode.SUCCESS;
  private  String message = "";

  public BaseResponse(T data ,String meessage ,String errorCode){
    this.data = data;
    this.message = meessage;
    this.notification = errorCode;
  }

}
