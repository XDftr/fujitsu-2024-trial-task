package ee.fujitsu.trialtask.cronjob;

import ee.fujitsu.trialtask.dto.ObservationsDto;
import ee.fujitsu.trialtask.service.ObservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherTaskScheduler {
    private final ObservationService observationService;

    /**
     * Retrieve weather data from the REST API and save it to the database.
     * This method is scheduled to run at the specified cron schedule.
     */
    @Scheduled(cron = "${weather.cron.schedule2}")
    public void fetchWeatherData() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ObservationsDto observationsDto = restTemplate
                    .getForObject("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php",
                            ObservationsDto.class);

            if (observationsDto != null) {
                observationService.addObservations(observationsDto);
                log.info("Saved observations");
            } else {
                log.error("Observations weren't saved");
            }

        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }
    }
}
