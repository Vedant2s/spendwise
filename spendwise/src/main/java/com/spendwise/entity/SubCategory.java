import jakarta.persistence.*;

@Entity
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryID;

    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

}