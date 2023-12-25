package com.management_user.dtos;


import com.management_user.entity.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageDTO implements Serializable {
    private Long id;
    private String packageName;
    private String packageCode;
    private String description;
    private Long price;
    private List<StudentDTO> students;
}
