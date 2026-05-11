import jakarta.persistence.*;

@Entity
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    private String categoryName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}