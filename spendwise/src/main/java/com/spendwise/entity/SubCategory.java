import jakarta.persistence.*;

@Entity
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryID;

    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<SubCategoryBudget> subCategoryBudgets;

    @OneToMany(mappedBy = "subCategory")
    private List<Transaction> transactions;
}