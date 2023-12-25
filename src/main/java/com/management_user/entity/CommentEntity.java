package com.management_user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments_entity")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentEntity extends Base {
    private String commentContent;
    private String time;
    private Boolean status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
