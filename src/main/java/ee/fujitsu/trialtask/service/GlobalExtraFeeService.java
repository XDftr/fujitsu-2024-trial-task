package ee.fujitsu.trialtask.service;

import ee.fujitsu.trialtask.entity.GlobalExtraFee;
import ee.fujitsu.trialtask.exception.NotFoundException;
import ee.fujitsu.trialtask.repository.GlobalExtraFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GlobalExtraFeeService {
    private final GlobalExtraFeeRepository globalExtraFeeRepository;

    /**
     * Returns the value of the global extra fee based on the provided condition.
     *
     * @param condition the condition to search for
     * @return the value of the global extra fee
     * @throws NotFoundException if the global extra fee condition is not found
     */
    public double getGlobalExtraFeeValueByCondition(String condition) {
        GlobalExtraFee globalExtraFee = globalExtraFeeRepository.findByCondition(condition).orElseThrow(
                () -> new NotFoundException("Global extra fee condition not found")
        );
        return globalExtraFee.getFeeValue();
    }
}
