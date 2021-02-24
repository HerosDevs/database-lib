package net.herospvp.database.lib.items;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface Papers {

    void writePaper(
            @NotNull Connection connection,
            @NotNull Instrument instrument
    );

}
