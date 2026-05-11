import jakarta.persistence.*;  // ✅ Required for JPA annotations
import java.math.BigDecimal;

@Entity  // ✅ Marks this class as a JPA entity → mapped to a database table
public class User {

    @Id 
    // ✅ Declares this field as the Primary Key of the table
    // → uniquely identifies each User record

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ✅ Auto-generates the ID using database auto-increment
    // → Database (MySQL/Postgres) assigns IDs like 1, 2, 3...
    // → Best for simple applications like SpendWise

    private Long userID;

    private String username;
    // ✅ Normal column (automatically mapped)
    // → No annotation needed for simple fields

    private String email;
    // ✅ Should ideally be UNIQUE (add @Column(unique = true) later)

    private String passwordHash;
    // ✅ Stores hashed password (NOT plain password)
    // → Used for secure authentication

    @Column(precision = 12, scale = 2)
    // ✅ Maps BigDecimal to DECIMAL(12,2) in DB
    // → precision = total digits (12)
    // → scale = digits after decimal (2)
    // Example: 1234567890.12
    // ✅ Ensures accurate storage for financial data

    private BigDecimal monthlyIncome;
}