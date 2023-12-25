package com.management_user.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class StudentDTO implements Serializable {
  private Long id;
  private String email;
  private String password;
  private String name;
  private String phone;
  private String address;
  private Long paymentId;
  private String createdAt;
  private String lastViewAt;
}
