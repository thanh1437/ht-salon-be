package com.salon.ht.mapper;

import com.salon.ht.entity.Service;
import com.salon.ht.entity.payload.ServiceResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ServiceResMapper extends EntityMapper<ServiceResponse, Service> {

}
