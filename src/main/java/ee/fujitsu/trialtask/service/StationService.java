package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    /**
     * Finds a station by the given city.
     *
     * @param city the city to search for
     * @return the station that matches the given city
     * @throws NotFoundException if there is no station in the specified city
     */
    public Station findStationByCity(String city) {
        return stationRepository.findByNameContaining(city).orElseThrow(
                () -> new NotFoundException("There's no station in that city")
        );
    }
}
