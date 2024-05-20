import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Product> findAll() {
        var sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();

        try (var connection = dbConnection.tryConnection(); PreparedStatement pstmnt = connection.prepareStatement(sql)) {
            var resultSet = pstmnt.executeQuery();
            while (resultSet.next()) {
                var product = extractProduct(resultSet);
                products.add(product);
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve products from database", e);
        }
        return products;
    }

    @Override
    public void update(Product product) {
        var sql = "UPDATE product SET " +
                "name = ?, " +
                "description = ?, " +
                "price = ?, " +
                "version = ?, " +
                "date_last_updated = ? " +
                "WHERE id = ?";

        try (var connection = dbConnection.tryConnection(); PreparedStatement pstmnt = connection.prepareStatement(sql)) {
            pstmnt.setString(1, product.getName());
            pstmnt.setString(2, product.getDescription());
            pstmnt.setBigDecimal(3, product.getPrice());
            pstmnt.setLong(4, product.getVersion());
            pstmnt.setTimestamp(5, Timestamp.valueOf(product.getDateLastUpdated()));
            pstmnt.setLong(6, product.getId());

            pstmnt.execute();
            System.out.println("Product info updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Unable to update product info in database", e);
        }
    }

    @Override
    public void delete(Long id) {
        var sql = "DELETE FROM product WHERE id = ?";

        try (var connection = dbConnection.tryConnection(); PreparedStatement pstmnt = connection.prepareStatement(sql)) {
            pstmnt.setLong(1, id);
            pstmnt.execute();
            System.out.println("Product info deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete product info from database", e);
        }
    }

    private Product extractProduct(ResultSet resultSet) throws SQLException {
        var product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setVersion(resultSet.getLong("version"));
        var dateCreated = resultSet.getTimestamp("date_created").toLocalDateTime();
        var dateLastUpdated = resultSet.getTimestamp("date_last_updated").toLocalDateTime();
        product.setDateCreated(dateCreated);
        product.setDateLastUpdated(dateLastUpdated);
        return product;
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
