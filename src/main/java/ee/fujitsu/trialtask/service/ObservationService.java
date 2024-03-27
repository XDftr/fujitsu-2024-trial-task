package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.dto.ObservationsDto;
import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.mapper.ObservationMapper;
import ee.fujitsu.trialtask.mapper.StationMapper;
import ee.fujitsu.trialtask.repository.ObservationRepository;
import ee.fujitsu.trialtask.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final StationRepository stationRepository;

    private final StationMapper stationMapper;
    private final ObservationMapper observationMapper;

    private final List<String> observableStations = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

    /**
     * Adds observations to the database.
     *
     * @param dto The ObservationsDto containing the observations to be added.
     */
    public void addObservations(ObservationsDto dto) {
        dto.getStation().stream()
                .filter(d -> observableStations.contains(d.getName()))
                .forEach(d -> {
                    Station station = stationRepository.findByName(d.getName())
                            .orElseGet(() -> stationRepository.save(stationMapper.toStationFromDto(d)));
                    observationRepository.save(observationMapper.toObservation(d, station, dto.getTimestamp()));
                });
    }
}
