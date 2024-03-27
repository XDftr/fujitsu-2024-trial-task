package ee.fujitsu.trialtask.mapper;

import ee.fujitsu.trialtask.dto.StationDto;
import ee.fujitsu.trialtask.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StationMapper {
    Station toStationFromDto(StationDto dto);
}
