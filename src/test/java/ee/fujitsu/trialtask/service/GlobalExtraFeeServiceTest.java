package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.GlobalExtraFee;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.GlobalExtraFeeRepository;
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
class GlobalExtraFeeServiceTest {
    @Mock
    private GlobalExtraFeeRepository globalExtraFeeRepository;

    @InjectMocks
    private GlobalExtraFeeService globalExtraFeeService;

    @Test
    void getGlobalExtraFeeValueByCondition_WhenConditionExists_ReturnsFeeValue() {
        double expectedFeeValue = 0.5;
        String condition = "WindSpeedBetween10And20";
        GlobalExtraFee globalExtraFee = new GlobalExtraFee();
        globalExtraFee.setCondition(condition);
        globalExtraFee.setFeeValue(expectedFeeValue);
        Mockito.when(globalExtraFeeRepository.findByCondition(condition))
                .thenReturn(Optional.of(globalExtraFee));

        double actualFeeValue = globalExtraFeeService.getGlobalExtraFeeValueByCondition(condition);

        assertEquals(expectedFeeValue, actualFeeValue);
    }

    @Test
    void getGlobalExtraFeeValueByCondition_WhenConditionDoesNotExist_ThrowsNotFoundException() {
        Mockito.when(globalExtraFeeRepository.findByCondition(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> globalExtraFeeService
                .getGlobalExtraFeeValueByCondition("NonExistentCondition"));
    }
}
