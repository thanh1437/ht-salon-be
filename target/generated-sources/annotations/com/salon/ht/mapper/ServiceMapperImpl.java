package com.salon.ht.mapper;

import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Service;
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
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public Service toEntity(ServiceDto dto) {
        if ( dto == null ) {
            return null;
        }

        Service service = new Service();

        service.setId( dto.getId() );
        service.setName( dto.getName() );
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
    public ServiceDto toDto(Service entity) {
        if ( entity == null ) {
            return null;
        }

        ServiceDto serviceDto = new ServiceDto();

        serviceDto.setCreatedDate( mapLocalDateTimeToString( entity.getCreatedDate() ) );
        serviceDto.setModifiedDate( mapLocalDateTimeToString( entity.getModifiedDate() ) );
        serviceDto.setId( entity.getId() );
        serviceDto.setName( entity.getName() );
        serviceDto.setType( entity.getType() );
        serviceDto.setPrice( entity.getPrice() );
        serviceDto.setDuration( entity.getDuration() );
        serviceDto.setStatus( entity.getStatus() );
        serviceDto.setCreateBy( entity.getCreateBy() );
        serviceDto.setModifiedBy( entity.getModifiedBy() );

        return serviceDto;
    }

    @Override
    public List<Service> toEntity(List<ServiceDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Service> list = new ArrayList<Service>( dtoList.size() );
        for ( ServiceDto serviceDto : dtoList ) {
            list.add( toEntity( serviceDto ) );
        }

        return list;
    }

    @Override
    public List<ServiceDto> toDto(List<Service> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ServiceDto> list = new ArrayList<ServiceDto>( entityList.size() );
        for ( Service service : entityList ) {
            list.add( toDto( service ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Service entity, ServiceDto dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
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
