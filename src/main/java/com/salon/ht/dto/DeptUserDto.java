package com.salon.ht.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeptUserDto implements Serializable {
    private Long id;
    private Long userId;
    private String name;
    private String position;
    private String jobTitle;
    private boolean isAdmin;
    private boolean isRoot;
}
