package ee.fujitsu.trialtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "regional_base_fee")
public class RegionalBaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regional_base_fee_id")
    private Integer regionalBaseFeeId;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "rbf", nullable = false)
    private Double rbf;
}
