package net.herospvp.database.items;

import org.jetbrains.annotations.Nullable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class Instrument {

    @Getter
    private final DataSource dataSource;
    @Getter
    private final String ip, port, user, password, database, flags, url, driverClassName;

    public Instrument(String ip, String port, String user, String password, String database, @Nullable String flags,
                      @Nullable String url, @Nullable String driverClassName,
                      @Nullable Map<String, String> properties, boolean useDefaults, int poolSize) {

        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.flags = flags;
        this.url = url;
        this.driverClassName = driverClassName;

        HikariConfig config = new HikariConfig();

        if (driverClassName != null) {
            config.setDriverClassName(driverClassName);
        }

        config.setJdbcUrl(
                url == null ? "jdbc:mysql" : url +
                "://" + ip + ":" + port + "/" + database
                + (flags == null ? "" : flags)
        );

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
            int count = Runtime.getRuntime().availableProcessors() * 2;
            System.out.println("[database-lib] Using default max-pool-size: " + count);
            config.setMaximumPoolSize(count);
        }

        if (properties != null) {
            properties.forEach(config::addDataSourceProperty);
        }

        dataSource = new HikariDataSource(config);
    }

    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (connection != null) try {
            connection.close();
        } catch (Exception ignored) {
        }
        if (preparedStatement != null) try {
            preparedStatement.close();
        } catch (Exception ignored) {
        }
        if (resultSet != null) try {
            resultSet.close();
        } catch (Exception ignored) {
        }
    }

}
