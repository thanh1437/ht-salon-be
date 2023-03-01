package com.salon.ht.mapper;

import com.salon.ht.entity.Service;
import com.salon.ht.entity.payload.ServiceResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-01T14:58:38+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
@Component
public class ServiceResMapperImpl implements ServiceResMapper {

    @Override
    public Service toEntity(ServiceResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Service service = new Service();

        service.setId( dto.getId() );
        service.setName( dto.getName() );
        service.setCode( dto.getCode() );
        service.setType( dto.getType() );
        service.setPrice( dto.getPrice() );
        service.setDuration( dto.getDuration() );
        service.setStatus( dto.getStatus() );
        service.setCreateBy( dto.getCreateBy() );
        service.setModifiedBy( dto.getModifiedBy() );
        if ( dto.getCreatedDate() != null ) {
            service.setCreatedDate( LocalDateTime.parse( dto.getCreatedDate() ) );
        }
        if ( dto.getModifiedDate() != null ) {
            service.setModifiedDate( LocalDateTime.parse( dto.getModifiedDate() ) );
        }

        return service;
    }

    @Override
    public ServiceResponse toDto(Service entity) {
        if ( entity == null ) {
            return null;
        }

        ServiceResponse serviceResponse = new ServiceResponse();

        serviceResponse.setCreatedDate( mapLocalDateTimeToString( entity.getCreatedDate() ) );
        serviceResponse.setModifiedDate( mapLocalDateTimeToString( entity.getModifiedDate() ) );
        serviceResponse.setId( entity.getId() );
        serviceResponse.setName( entity.getName() );
        serviceResponse.setCode( entity.getCode() );
        serviceResponse.setType( entity.getType() );
        serviceResponse.setPrice( entity.getPrice() );
        serviceResponse.setDuration( entity.getDuration() );
        serviceResponse.setStatus( entity.getStatus() );
        serviceResponse.setCreateBy( entity.getCreateBy() );
        serviceResponse.setModifiedBy( entity.getModifiedBy() );

        return serviceResponse;
    }

    @Override
    public List<Service> toEntity(List<ServiceResponse> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Service> list = new ArrayList<Service>( dtoList.size() );
        for ( ServiceResponse serviceResponse : dtoList ) {
            list.add( toEntity( serviceResponse ) );
        }

        return list;
    }

    @Override
    public List<ServiceResponse> toDto(List<Service> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ServiceResponse> list = new ArrayList<ServiceResponse>( entityList.size() );
        for ( Service service : entityList ) {
            list.add( toDto( service ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Service entity, ServiceResponse dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getCode() != null ) {
            entity.setCode( dto.getCode() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }
        if ( dto.getDuration() != null ) {
            entity.setDuration( dto.getDuration() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getCreateBy() != null ) {
            entity.setCreateBy( dto.getCreateBy() );
        }
        if ( dto.getModifiedBy() != null ) {
            entity.setModifiedBy( dto.getModifiedBy() );
        }
        if ( dto.getCreatedDate() != null ) {
            entity.setCreatedDate( LocalDateTime.parse( dto.getCreatedDate() ) );
        }
        if ( dto.getModifiedDate() != null ) {
            entity.setModifiedDate( LocalDateTime.parse( dto.getModifiedDate() ) );
        }
    }
}
