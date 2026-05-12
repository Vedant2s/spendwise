import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    private String categoryName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable=false)
    private User user;

    @OneToMany(mappedBy = "category")
    private List<CategoryBudget> categoryBudgets;

    @OneToMany(mappedBy = "category")
    private List<SubCategory> subCategories;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

}