package ee.fujitsu.trialtask.repository;

import ee.fujitsu.trialtask.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
}
