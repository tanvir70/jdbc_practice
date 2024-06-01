package cp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        final ExecutorService executorService = Executors.newFixedThreadPool(25);

        IntStream.range(0, 200)
                .forEach(iteration -> {
                    executorService.submit(() -> {
                        executeCountQuery(iteration + 1);
                    });
                });
    }

    private static void executeCountQuery(int iteration) {
        LOGGER.info("Iteration: {}", iteration);
        var sql = "select count(*) from product";
        try (var connection = DataSource.getConnection()) {
            var statement = connection.prepareStatement(sql);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var count = resultSet.getLong(1);
                LOGGER.info("Total row in product table: {}", count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}