package net.herospvp.database.items;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Instrument {

    private DataSource dataSource;
    private final String ip;
    private final String port;
    private final String database;
    private final String user;
    private final String password;
    private final String url, driver;
    @Nullable
    private final Map<String, String> proprieties;
    private final boolean useDefaults;
    private final int poolSize;

    public void assemble() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(driver);
        config.setJdbcUrl("jdbc:" + url + "://" + ip + ":" + port + "/" + database);

        config.setUsername(user);
        config.setPassword(password);

        if (useDefaults) {
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }

        if (poolSize != 0) {
            config.setMaximumPoolSize(poolSize);
        } else {
            config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        }

        if (proprieties != null) {
            proprieties.forEach(config::addDataSourceProperty);
        }

        dataSource = new HikariDataSource(config);
    }

    public void close(Connection connection) {
        if (connection != null) try {
            connection.close();
        } catch (Exception ignored) {
        }
    }

    public void close(PreparedStatement preparedStatement) {
        if (preparedStatement != null) try {
            preparedStatement.close();
        } catch (Exception ignored) {
        }
    }

    public void close(ResultSet resultSet) {
        if (resultSet != null) try {
            resultSet.close();
        } catch (Exception ignored) {
        }
    }

    public void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        close(preparedStatement);
        close(resultSet);
    }

    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        close(preparedStatement);
        close(resultSet);
        close(connection);
    }

}
