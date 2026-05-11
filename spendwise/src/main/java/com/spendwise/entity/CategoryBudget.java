import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class CategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryBudgetID;

    private BigDecimal maxAllotedBudget; // Use BigDecimal for accurate financial calculations

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = true)
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}