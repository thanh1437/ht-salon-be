package com.salon.ht.mapper;

import com.salon.ht.config.Constant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityMapper<D, E> {
    E toEntity(D dto);

    @Mappings({
            @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "mapLocalDateTimeToString"),
            @Mapping(source = "modifiedDate", target = "modifiedDate", qualifiedByName = "mapLocalDateTimeToString")
    })
    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    @Mappings({
            @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "mapLocalDateTimeToString"),
            @Mapping(source = "modifiedDate", target = "modifiedDate", qualifiedByName = "mapLocalDateTimeToString")
    })
    List<D> toDto(List<E> entityList);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);

    @Named("mapLocalDateTimeToString")
    default String mapLocalDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(Constant.DATE_FORMAT);
    }
}
