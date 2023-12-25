package com.management_user.dtos;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LessonDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String content;
    private Long displayIndex;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseDTO course;
}
