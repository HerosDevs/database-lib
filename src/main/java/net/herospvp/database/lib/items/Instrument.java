package net.herospvp.database.lib.items;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@SuppressWarnings("unused")
@Getter
public class Instrument {

    private DataSource dataSource;
    private final String ip, port, database, user, password, url, driver;
    private final Map<String, String> properties;
    private final boolean useDefaults;
    private final int poolSize;

    public Instrument(
            @NotNull String ip,
            @NotNull String port,
            @NotNull String database,
            @NotNull String user,
            @Nullable String password,
            @NotNull String url,
            @NotNull String driver,
            @Nullable Map<String, String> properties,
            boolean useDefaults,
            int poolSize
    ) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.properties = properties;
        this.useDefaults = useDefaults;
        this.poolSize = poolSize;
        init();
    }

    private void init() {
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

        if (properties != null) {
            properties.forEach(config::addDataSourceProperty);
        }

        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public <G> void close(G generic) {
        if (generic instanceof PreparedStatement) {
            ((PreparedStatement) generic).close();
        }
        else if (generic instanceof Connection) {
            ((Connection) generic).close();
        }
        else if (generic instanceof ResultSet) {
            ((ResultSet) generic).close();
        }
    }

    public <G> void close(G generic, G generic1) {
        close(generic);
        close(generic1);
    }

    public <G> void close(G generic, G generic1, G generic2) {
        close(generic);
        close(generic1);
        close(generic2);
    }

}
