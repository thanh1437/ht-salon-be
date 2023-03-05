package com.salon.ht.mapper;

import com.salon.ht.entity.Combo;
import com.salon.ht.entity.payload.ComboResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ComboResMapper extends EntityMapper<ComboResponse, Combo> {
}
