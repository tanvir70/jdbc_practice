import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductRepositoryImpl implements ProductRepository{

    private DBConnection dbConnection = new DBConnection();
    @Override
    public void save(Product product) {
        var sql = String.format("INSERT INTO product (name, description, price, date_created, date_last_updated) VALUES ('%S', '%S', %s, '%s', '%s')",
        product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getVersion(),
                Timestamp.valueOf(product.getDateCreated()),
                Timestamp.valueOf(product.getDateLastUpdated()));

        try (var connection = dbConnection.tryConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate(sql);
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
