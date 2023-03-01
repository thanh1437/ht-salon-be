package com.salon.ht.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto implements Serializable {
    private Long id;
    private String name;
    private String departmentExtId;
    private String departmentPath;
    private String shortName;
    private boolean status;
    private Date createdDate;
    private Date modifiedDate;
    private Long parentId;
    private Set<DeptUserDto> users;
}
