package ee.fujitsu.trialtask.repository;

import ee.fujitsu.trialtask.entity.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Integer> {
    Optional<RegionalBaseFee> findByCityAndVehicleType(String city, String vehicleType);
}
