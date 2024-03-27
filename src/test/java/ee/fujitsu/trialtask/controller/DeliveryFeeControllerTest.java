package ee.fujitsu.trialtask.controller;

import ee.fujitsu.trialtask.exception.DeliveryFeeException;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.service.DeliveryFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DeliveryFeeController.class)
class DeliveryFeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryFeeService deliveryFeeService;

    @Test
    void getDeliveryFee_WhenSuccessful_ReturnsFee() throws Exception {
        given(deliveryFeeService.calculateDeliveryFee("Test city", "Bike")).willReturn(4.0);

        mockMvc.perform(get("/delivery-fee")
                        .param("city", "Test city")
                        .param("vehicleType", "Bike")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("4.0"));
    }

    @Test
    void getDeliveryFee_WhenCityOrVehicleTypeIsInvalid_ReturnsNotFound() throws Exception {
        given(deliveryFeeService.calculateDeliveryFee("Unknown city", "Helicopter"))
                .willThrow(new NotFoundException("There's no station in that city"));

        mockMvc.perform(get("/delivery-fee")
                        .param("city", "Unknown city")
                        .param("vehicleType", "Helicopter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDeliveryFee_WhenDeliveryConditionIsForbidden_ReturnsBadRequest() throws Exception {
        given(deliveryFeeService.calculateDeliveryFee("Test city", "Bike"))
                .willThrow(new DeliveryFeeException("Vehicle forbidden due to weather conditions"));

        mockMvc.perform(get("/delivery-fee")
                        .param("city", "Test city")
                        .param("vehicleType", "Bike")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
