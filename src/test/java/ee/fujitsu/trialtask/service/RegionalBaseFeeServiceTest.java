package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.RegionalBaseFee;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.RegionalBaseFeeRepository;
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
class RegionalBaseFeeServiceTest {
    @Mock
    private RegionalBaseFeeRepository regionalBaseFeeRepository;

    @InjectMocks
    private RegionalBaseFeeService regionalBaseFeeService;

    @Test
    void getRbf_WhenCityAndVehicleTypeExist_ReturnsBaseFee() {
        String city = "Test city";
        String vehicleType = "Bike";
        double expectedRbf = 1.5;
        RegionalBaseFee regionalBaseFee = new RegionalBaseFee();
        regionalBaseFee.setCity(city);
        regionalBaseFee.setVehicleType(vehicleType);
        regionalBaseFee.setRbf(expectedRbf);
        Mockito.when(regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType))
                .thenReturn(Optional.of(regionalBaseFee));

        double actualRbf = regionalBaseFeeService.getRbf(city, vehicleType);

        assertEquals(expectedRbf, actualRbf);
    }

    @Test
    void getRbf_WhenCityAndVehicleTypeDoNotExist_ThrowsNotFoundException() {
        Mockito.when(regionalBaseFeeRepository.findByCityAndVehicleType(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> regionalBaseFeeService.getRbf("Test city", "Test vehicle"));
    }

}
