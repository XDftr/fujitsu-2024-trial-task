package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.RegionalBaseFee;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.RegionalBaseFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegionalBaseFeeService {
    private final RegionalBaseFeeRepository regionalBaseFeeRepository;

    /**
     * Retrieves the regional base fee for a given city and vehicle type.
     *
     * @param city        the city for which to retrieve the regional base fee
     * @param vehicleType the type of vehicle for which to retrieve the regional base fee
     * @return the regional base fee for the specified city and vehicle type
     * @throws NotFoundException if no regional base fee is found for the given city and vehicle type
     */
    public double getRbf(String city, String vehicleType) {
        RegionalBaseFee regionalBaseFee = regionalBaseFeeRepository.findByCityAndVehicleType(city, vehicleType)
                .orElseThrow(() -> new NotFoundException("No regional base fee for that city - vehicle type pair"));
        return regionalBaseFee.getRbf();
    }
}
