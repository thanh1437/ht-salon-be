package com.salon.ht.mapper;

import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Service;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ServiceMapper extends EntityMapper<ServiceDto, Service> {
}
