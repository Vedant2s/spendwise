import java.math.BigDecimal;

public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    private String username;
    private String email;
    private String passwordHash;
    @Column(precision = 12, scale = 2)
    private BigDecimal monthlyIncome;
}