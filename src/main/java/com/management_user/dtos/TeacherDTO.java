package com.management_user.dtos;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class TeacherDTO implements Serializable {
    private Long id;
    private String name;
    private String avatar; //URL
    private String email;
    private String password;

    private String phone;
    private String address;
    private String createdAt;
    private String lastViewAt;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseDTO course;

//    @JsonIgnore
//    private MultipartFile file;
}
