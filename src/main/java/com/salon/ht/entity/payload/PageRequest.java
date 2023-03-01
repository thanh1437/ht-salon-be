package com.salon.ht.entity.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Setter
@Getter
public class PageRequest implements Serializable {
    private int page;
    private int limit;
    private String name;
}
