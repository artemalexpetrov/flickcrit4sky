package com.flickcrit.app.infrastructure.core.conversion.config;

import com.flickcrit.app.infrastructure.core.conversion.mapper.EntityIdMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {
        ConversionServiceAdapter.class,
        EntityIdMapper.class
    }
)
@SpringMapperConfig(conversionServiceAdapterPackage = "com.flickcrit.app.infrastructure.core.conversion.config")
public interface MapStructConfig {
}
