import jakarta.persistence.*;

@Entity
public class SubCategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryBudgetID;

    private BigDecimal maxAllotedBudget; // Use BigDecimal for accurate financial calculations

    @ManyToOne
    @JoinColumn(name = "sub_category_id",nullable = true)
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}