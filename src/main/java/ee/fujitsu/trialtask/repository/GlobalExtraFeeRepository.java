package ee.fujitsu.trialtask.repository;

import ee.fujitsu.trialtask.entity.GlobalExtraFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalExtraFeeRepository extends JpaRepository<GlobalExtraFee, Integer> {
    Optional<GlobalExtraFee> findByCondition(String condition);
}
