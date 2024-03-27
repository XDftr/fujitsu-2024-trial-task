package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.Observation;
import ee.fujitsu.trialtask.entity.Station;
import ee.fujitsu.trialtask.exception.DeliveryFeeException;
import ee.fujitsu.trialtask.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private GlobalExtraFeeService globalExtraFeeService;

    @Mock
    private RegionalBaseFeeService regionalBaseFeeService;

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    @BeforeEach
    void setup() {
        Mockito.lenient().when(regionalBaseFeeService.getRbf(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1d);
        Mockito.lenient().when(globalExtraFeeService.getGlobalExtraFeeValueByCondition("WindSpeedBetween10And20"))
                .thenReturn(0.5);
        Mockito.lenient().when(globalExtraFeeService.getGlobalExtraFeeValueByCondition("AirTemperatureBelow-10"))
                .thenReturn(1d);
        Mockito.lenient().when(globalExtraFeeService.getGlobalExtraFeeValueByCondition("AirTemperatureBetween0And-10"))
                .thenReturn(0.5);
        Mockito.lenient().when(globalExtraFeeService.getGlobalExtraFeeValueByCondition("PhenomenonSnowOrSleet"))
                .thenReturn(1d);
        Mockito.lenient().when(globalExtraFeeService.getGlobalExtraFeeValueByCondition("PhenomenonRain"))
                .thenReturn(0.5);
    }

    @Test
    void calculateDeliveryFee_WhenBikeNormalCondition_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(1, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeTemperatureBetweenZeroAndTen_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(-2.1,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(1.5, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeTempLessThanMin_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(-11,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeWindSpeedBetweenTenAndTwenty_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                15, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(1.5, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeWindSpeedMoreThanTwenty_ExpectDeliveryFeeException() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                23, ""));
        Assertions.assertThrows(DeliveryFeeException.class, () ->
                deliveryFeeService.calculateDeliveryFee("Test city", "Bike"));
    }

    @Test
    void calculateDeliveryFee_WhenBikeWithSnowWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                4, "Heavy snow shower"));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeWithSleetWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                4, "Light sleet"));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeWithHeavyRainWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                4, "Heavy rain"));
        double fee = deliveryFeeService.calculateDeliveryFee("Test city", "Bike");
        Assertions.assertEquals(1.5, fee);
    }

    @Test
    void calculateDeliveryFee_WhenBikeWithGlazeWeather_ThrowsDeliveryFeeException() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(9,
                4, "Glaze"));
        Assertions.assertThrows(DeliveryFeeException.class, () ->
                deliveryFeeService.calculateDeliveryFee("Test city", "Bike"));
    }

    @Test
    void calculateDeliveryFee_WhenScooterNormalCondition_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(1, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterTemperatureBetweenZeroAndTen_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(-3,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(1.5, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterTempLessThanMin_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(-11,
                4.7, ""));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterWithSnowWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, "Light snow shower"));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterWithSleetWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, "Light sleet"));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(2, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterWithHeavyRainWeather_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, "Heavy rain"));
        double fee = deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter");
        Assertions.assertEquals(1.5, fee);
    }

    @Test
    void calculateDeliveryFee_WhenScooterWithGlazeWeather_ThrowsDeliveryFeeException() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(6,
                4.7, "Glaze"));
        Assertions.assertThrows(DeliveryFeeException.class,
                () -> deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter"));
    }

    @Test
    void calculateDeliveryFee_WhenObservationsMissing_ThrowsNotFoundException() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation());
        Assertions.assertThrows(NotFoundException.class,
                () -> deliveryFeeService.calculateDeliveryFee("Tartu", "Scooter"));
    }

    @Test
    void calculateDeliveryFee_WhenCarAnyCondition_ReturnsRbf() {
        Mockito.when(stationService.findStationByCity(Mockito.anyString())).thenReturn(getStation(-2.1,
                4.7, "Light snow shower"));
        Assertions.assertEquals(1.0, deliveryFeeService.calculateDeliveryFee("Test City", "Car"));
    }

    private Station getStation() {
        Station station = new Station();
        station.setName("Test city");
        station.setWmocode(122154);
        station.setObservations(new HashSet<>());
        return station;
    }

    private Station getStation(double airTemperature, double windSpeed, String phenomenon) {
        Station station = new Station();
        station.setName("Test city");
        station.setWmocode(122154);
        station.setObservations(getObservations(airTemperature, windSpeed, phenomenon));
        return station;
    }

    private Set<Observation> getObservations(double airTemperature, double windSpeed, String phenomenon) {
        Set<Observation> observations = new HashSet<>();
        Observation observation = new Observation();
        observation.setTimestamp(Instant.now().toEpochMilli());
        observation.setAirTemperature(airTemperature);
        observation.setWindSpeed(windSpeed);
        observation.setPhenomenon(phenomenon);
        observations.add(observation);
        return observations;
    }
}
