package com.salon.ht.mapper;

import com.salon.ht.dto.ComboDto;
import com.salon.ht.entity.Combo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ComboMapper extends EntityMapper<ComboDto, Combo> {

}
