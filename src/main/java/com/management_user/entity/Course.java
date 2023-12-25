package com.management_user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Course extends Base{
    private String name;
    private String categoryName;
    private String description;
    private Boolean status;


    @OneToMany(mappedBy = "course") //sẽ map vào (private Teacher teacher) ở @ManyToOne bên entity Course
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "course") //sẽ map vào (private Teacher teacher) ở @ManyToOne bên entity Course
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course") //sẽ map vào (private Teacher teacher) ở @ManyToOne bên entity Course
    private List<CommentEntity> commentEntities = new ArrayList<>();


//    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    @JoinTable(name = "course_lesson",
//            joinColumns = @JoinColumn(name = "course_id"),
//            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
//    private List<Lesson> lesons;

}
