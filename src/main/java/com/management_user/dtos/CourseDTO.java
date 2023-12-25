package com.management_user.dtos;

import com.management_user.entity.Teacher;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CourseDTO implements Serializable {
    private  Long id;
    private String name;
    private String categoryName;
    private String description;
    private Boolean status;

}
