package com.management_user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teachers")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Teacher extends  Base {
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
    private Course course;

}
