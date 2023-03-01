package com.salon.ht.mapper.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserCSV {
    private String username;
    private String name;
    private String email;
    private String mobile;
    private String photo;
    private String departmentPath;
    private String password;
    private String jobTile;
    private String position;
}
