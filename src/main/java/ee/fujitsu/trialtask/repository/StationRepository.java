package ee.fujitsu.trialtask.repository;

import ee.fujitsu.trialtask.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Integer> {
    Optional<Station> findByName(String name);
    Optional<Station> findByNameContaining(String name);
}
