package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.dto.ObservationsDto;
import ee.fujitsu.trialtask.dto.StationDto;
import ee.fujitsu.trialtask.entity.Observation;
import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.mapper.ObservationMapper;
import ee.fujitsu.trialtask.mapper.StationMapper;
import ee.fujitsu.trialtask.repository.ObservationRepository;
import ee.fujitsu.trialtask.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObservationServiceTest {
    @Mock
    private ObservationRepository observationRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationMapper stationMapper;

    @Mock
    private ObservationMapper observationMapper;

    @InjectMocks
    private ObservationService observationService;

    private ObservationsDto observationsDto;
    private StationDto stationDto;

    private Station station;

    @BeforeEach
    void setUp() {
        stationDto = new StationDto();
        stationDto.setName("Tallinn-Harku");
        observationsDto = new ObservationsDto();
        observationsDto.setStation(List.of(stationDto));
        observationsDto.setTimestamp(System.currentTimeMillis());

        station = new Station();
        station.setName(stationDto.getName());


    }

    @Test
    void addObservations_WhenObservableStation_ObservationAdded() {
        when(stationRepository.save(any(Station.class))).thenReturn(station);
        when(stationMapper.toStationFromDto(any(StationDto.class))).thenReturn(station);
        when(observationMapper.toObservation(any(StationDto.class), any(Station.class), any(Long.class))).thenReturn(new Observation());

        observationService.addObservations(observationsDto);

        verify(observationRepository, times(1)).save(any());
    }

    @Test
    void addObservations_WhenNonObservableStation_ObservationNotAdded() {
        stationDto.setName("Narva");

        observationService.addObservations(observationsDto);

        verify(observationRepository, never()).save(any());
    }
}
