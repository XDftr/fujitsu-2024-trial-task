package ee.fujitsu.trialtask.mapper;

import ee.fujitsu.trialtask.dto.StationDto;
import ee.fujitsu.trialtask.entity.Observation;
import ee.fujitsu.trialtask.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {StationMapper.class})
public interface ObservationMapper {
    Observation toObservationFromStationDto(StationDto dto);

    default Observation toObservation(StationDto dto, Station station, Long timestamp) {
        Observation observation = toObservationFromStationDto(dto);
        observation.setStation(station);
        observation.setTimestamp(timestamp);
        return observation;
    }
}
