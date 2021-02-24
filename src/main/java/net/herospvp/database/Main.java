package net.herospvp.database;

import lombok.Getter;
import net.herospvp.database.lib.Director;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    private Director director;

    @Override
    public void onEnable() {
        director = new Director();
    }

    @Override
    public void onDisable() {

    }

}
