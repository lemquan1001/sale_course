package com.management_user.dtos;


import com.management_user.entity.Course;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CommentDTO implements Serializable {
    private Long id;
    private String commentContent;
    private String time;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseDTO course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentDTO student;
}
