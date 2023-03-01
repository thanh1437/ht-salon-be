package com.salon.ht.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeptDto implements Serializable {
    private Long id;
    private Long departmentId;
    private String departmentName;
    private String position;
    private String jobTitle;
    private boolean isAdmin;
    private boolean isRoot;
}
