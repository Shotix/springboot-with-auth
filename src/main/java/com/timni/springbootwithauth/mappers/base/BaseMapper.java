package com.timni.springbootwithauth.mappers.base;

import com.timni.springbootwithauth.mappers.annotations.ToEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseMapper<E, C, U, P, R> {

    @ToEntity
    E toEntity(C request);

    @ToEntity
    E updateWithRequest(U request, @MappingTarget E entity);

    @ToEntity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E patchWithRequest(P request, @MappingTarget E entity);

    R toResponse(E entity);

}
