import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductRepositoryImpl implements ProductRepository{

    private DBConnection dbConnection = new DBConnection();
    @Override
    public void save(Product product) {
        var sql = "INSERT INTO product ( " +
                "name, " +
                "description, " +
                "price, " +
                "version, " +
                "date_created, " +
                "date_last_updated) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (var connection = dbConnection.tryConnection(); PreparedStatement pstmnt = connection.prepareStatement(sql)) {
            pstmnt.setString(1, product.getName());
            pstmnt.setString(2, product.getDescription());
            pstmnt.setBigDecimal(3, product.getPrice());
            pstmnt.setLong(4, product.getVersion());
            pstmnt.setTimestamp(5, Timestamp.valueOf(product.getDateCreated()));
            pstmnt.setTimestamp(6, Timestamp.valueOf(product.getDateLastUpdated()));

            pstmnt.execute();
            System.out.println("Product info inserted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Unable to insert product info to database", e);
        }

    }

    public static void main(String[] args) {
        var product = new Product();
        product.setName("Laptop");
        product.setDescription("Dell Laptop");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setVersion(1L);
        product.setDateCreated(LocalDateTime.now());
        product.setDateLastUpdated(LocalDateTime.now());

        var productRepository = new ProductRepositoryImpl();
        productRepository.save(product);
    }
}
