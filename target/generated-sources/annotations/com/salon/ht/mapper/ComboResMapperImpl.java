package com.salon.ht.mapper;

import com.salon.ht.entity.Combo;
import com.salon.ht.entity.payload.ComboResponse;
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
public class ComboResMapperImpl implements ComboResMapper {

    @Override
    public Combo toEntity(ComboResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Combo combo = new Combo();

        combo.setId( dto.getId() );
        combo.setName( dto.getName() );
        combo.setCode( dto.getCode() );
        combo.setPrice( dto.getPrice() );
        combo.setStatus( dto.getStatus() );
        combo.setCreateBy( dto.getCreateBy() );
        combo.setModifiedBy( dto.getModifiedBy() );
        if ( dto.getCreatedDate() != null ) {
            combo.setCreatedDate( LocalDateTime.parse( dto.getCreatedDate() ) );
        }
        if ( dto.getModifiedDate() != null ) {
            combo.setModifiedDate( LocalDateTime.parse( dto.getModifiedDate() ) );
        }

        return combo;
    }

    @Override
    public ComboResponse toDto(Combo entity) {
        if ( entity == null ) {
            return null;
        }

        ComboResponse comboResponse = new ComboResponse();

        comboResponse.setCreatedDate( mapLocalDateTimeToString( entity.getCreatedDate() ) );
        comboResponse.setModifiedDate( mapLocalDateTimeToString( entity.getModifiedDate() ) );
        comboResponse.setId( entity.getId() );
        comboResponse.setName( entity.getName() );
        comboResponse.setCode( entity.getCode() );
        comboResponse.setPrice( entity.getPrice() );
        comboResponse.setStatus( entity.getStatus() );
        comboResponse.setCreateBy( entity.getCreateBy() );
        comboResponse.setModifiedBy( entity.getModifiedBy() );

        return comboResponse;
    }

    @Override
    public List<Combo> toEntity(List<ComboResponse> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Combo> list = new ArrayList<Combo>( dtoList.size() );
        for ( ComboResponse comboResponse : dtoList ) {
            list.add( toEntity( comboResponse ) );
        }

        return list;
    }

    @Override
    public List<ComboResponse> toDto(List<Combo> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ComboResponse> list = new ArrayList<ComboResponse>( entityList.size() );
        for ( Combo combo : entityList ) {
            list.add( toDto( combo ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Combo entity, ComboResponse dto) {
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
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
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
