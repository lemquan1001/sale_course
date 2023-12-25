package com.management_user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lessons")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Lesson extends Base{
    private  String name;
    private String description;
    private  String content;
    private Long displayIndex;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
