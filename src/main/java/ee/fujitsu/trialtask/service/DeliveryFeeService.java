package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.Observation;
import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.exception.DeliveryFeeException;
import ee.fujitsu.trialtask.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {
    private final StationService stationService;
    private final RegionalBaseFeeService regionalBaseFeeService;
    private final GlobalExtraFeeService globalExtraFeeService;
    private static final String BIKE = "Bike";
    private static final String SCOOTER = "Scooter";
    private static final String SELECTED_VEHICLE_FORBIDDEN = "Usage of selected vehicle type is forbidden";

    /**
     * Calculates the delivery fee based on the city and vehicle type.
     *
     * @param city        the city for which to calculate the delivery fee
     * @param vehicleType the type of vehicle for the delivery
     * @return the calculated delivery fee
     * @throws NotFoundException        if no observation or regional base fee is found for the given city
     * @throws DeliveryFeeException      if the selected vehicle is forbidden due to weather conditions
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        Station station = stationService.findStationByCity(city);
        Observation observation = station.getObservations().stream()
                .max(Comparator.comparing(Observation::getTimestamp))
                .orElseThrow(
                        () -> new NotFoundException("No observation for that city")
                );

        double rbf = regionalBaseFeeService.getRbf(city, vehicleType);

        double airTemperature = observation.getAirTemperature();
        double windSpeed = observation.getWindSpeed();
        String phenomenon = observation.getPhenomenon();

        if (vehicleType.equals(BIKE) && windSpeed > 20) {
            throw new DeliveryFeeException(SELECTED_VEHICLE_FORBIDDEN);
        }

        if ((vehicleType.equals(BIKE) || vehicleType.equals(SCOOTER))) {
            if (isProhibitedWeatherForBikesAndScooters(phenomenon)) {
                throw new DeliveryFeeException(SELECTED_VEHICLE_FORBIDDEN);
            } else {
                rbf = adjustRbfForTemperature(rbf, airTemperature);
                rbf = adjustRbfForPhenomenon(rbf, phenomenon);

                if (vehicleType.equals(BIKE) && windSpeed > 10) {
                    rbf += globalExtraFeeService
                            .getGlobalExtraFeeValueByCondition("WindSpeedBetween10And20");
                }
            }
        }

        return rbf;
    }

    private boolean isProhibitedWeatherForBikesAndScooters(String phenomenon) {
        List<String> glazeHailThunderPhenomenons = List.of("Glaze", "Hail", "Thunder", "Thunderstorm");
        return glazeHailThunderPhenomenons.contains(phenomenon);
    }

    private double adjustRbfForTemperature(double rbf, double airTemperature) {
        if (airTemperature < -10) {
            return rbf + globalExtraFeeService
                    .getGlobalExtraFeeValueByCondition("AirTemperatureBelow-10");
        } else if (airTemperature >= -10 && airTemperature < 0) {
            return rbf + globalExtraFeeService
                    .getGlobalExtraFeeValueByCondition("AirTemperatureBetween0And-10");
        }
        return rbf;
    }

    private double adjustRbfForPhenomenon(double rbf, String phenomenon) {
        List<String> snowSleetPhenomenons = List.of("Light snow shower", "Moderate snow shower", "Heavy snow shower",
                "Light sleet", "Moderate sleet", "Light snowfall", "Moderate snowfall", "Heavy snowfall",
                "Blowing snow", "Drifting snow");
        List<String> rainPhenomenons = List.of("Light shower", "Moderate shower", "Heavy shower", "Light rain",
                "Moderate rain", "Heavy rain");

        if (snowSleetPhenomenons.contains(phenomenon)) {
            return rbf + globalExtraFeeService
                    .getGlobalExtraFeeValueByCondition("PhenomenonSnowOrSleet");
        } else if (rainPhenomenons.contains(phenomenon)) {
            return rbf + globalExtraFeeService
                    .getGlobalExtraFeeValueByCondition("PhenomenonRain");
        }
        return rbf;
    }
}
