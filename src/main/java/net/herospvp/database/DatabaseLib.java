package net.herospvp.database;

import lombok.Getter;
import net.herospvp.database.commands.DatabaseCommand;
import net.herospvp.database.lib.Director;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class DatabaseLib extends JavaPlugin {

    private Director director;

    @Override
    public void onEnable() {
        director = new Director();
        new DatabaseCommand(this);
    }

    @Override
    public void onDisable() {

    }

}
