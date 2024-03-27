package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {
    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Test
    void findStationByCity_WhenStationExists_ReturnsStation() {
        String city = "Test city";
        Station expectedStation = new Station();
        expectedStation.setName(city);
        Mockito.when(stationRepository.findByNameContaining(city)).thenReturn(Optional.of(expectedStation));

        Station actualStation = stationService.findStationByCity(city);

        assertEquals(expectedStation, actualStation);
    }

    @Test
    void findStationByCity_WhenStationDoesNotExist_ThrowsNotFoundException() {
        String city = "Test city";
        Mockito.when(stationRepository.findByNameContaining(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stationService.findStationByCity(city));
    }
}
