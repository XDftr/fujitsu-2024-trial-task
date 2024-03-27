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
@Table(name = "global_extra_fee")
public class GlobalExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "global_extra_fee_id")
    private Integer globalExtraFeeId;

    @Column(name = "condition", nullable = false)
    private String condition;

    @Column(name = "fee_value", nullable = false)
    private Double feeValue;
}
