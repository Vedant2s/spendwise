import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;

    private String subscriptionName;

    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    private String billingCycle; // e.g., "Monthly", "Yearly"

    private String nextBillingDate; // Store as String for simplicity (e.g., "2024-07-01")
}