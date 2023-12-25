package com.management_user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "packages_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PackageEntity extends  Base{

    @Column(nullable = false)
    private String packageName;

    @Column(nullable = false)
    private String packageCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "package_student",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

}
