package ee.fujitsu.trialtask.controller;

import ee.fujitsu.trialtask.exception.DeliveryFeeException;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.service.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFeeService deliveryFeeService;

    /**
     * Calculates the delivery fee based on the city and vehicle type.
     *
     * @param city        the city for which to calculate the delivery fee
     * @param vehicleType the type of vehicle for the delivery
     * @return the calculated delivery fee
     * @throws NotFoundException       if no observation or regional base fee is found for the given city
     * @throws DeliveryFeeException     if the selected vehicle is forbidden due to weather conditions
     */
    @GetMapping
    public ResponseEntity<Double> getDeliveryFee(@RequestParam String city, @RequestParam String vehicleType) {
        return ResponseEntity.ok(deliveryFeeService.calculateDeliveryFee(city, vehicleType));
    }
}
