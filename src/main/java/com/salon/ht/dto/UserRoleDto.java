package com.salon.ht.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String mobile;
    private String email;
    private String photo;
}
