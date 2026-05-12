import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

public enum BillingCycle {
    MONTHLY,
    YEARLY
}

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;

    private String subscriptionName;

    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;
    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle; // e.g., MONTHLY, YEARLY

    private LocalDate nextBillingDate; //Stored as LocalDate for proper date handling
}