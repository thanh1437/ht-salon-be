package com.salon.ht.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {
    private Long id;
    private String name;
    private String detail;
    private String description;
    private boolean isAdminRole;
    private List<UserRoleDto> users;
}
