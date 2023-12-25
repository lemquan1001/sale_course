package com.management_user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Student extends Base {

  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String phone;
  @Column(nullable = false)
  private String address;
  @Column(nullable = false)
  private Long paymentId;
  @Column(nullable = false)
  private String createdAt;
  @Column(nullable = false)
  private String lastViewAt;

  @JsonIgnore
  @OneToMany(mappedBy = "student")
  private List<CommentEntity> commentEntities = new ArrayList<>();

}
