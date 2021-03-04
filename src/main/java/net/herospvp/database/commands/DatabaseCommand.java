package net.herospvp.database.commands;

import net.herospvp.database.DatabaseLib;
import net.herospvp.database.lib.Director;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DatabaseCommand implements CommandExecutor {

    private final DatabaseLib instance;

    public DatabaseCommand(DatabaseLib instance) {
        this.instance = instance;
        instance.getCommand("database").setExecutor(this);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] strings
    ) {

        Director director = instance.getDirector();

        commandSender.sendMessage(
                ChatColor.RED +
                "Versione: " + instance.getDescription().getVersion() +
                "\nInstruments totali: " + director.getInstrumentsCollection().size() +
                "\nMusicians totali: " + director.getMusicians().size() +
                "\nRepository: https://github.com/HerosDevs/database-lib"
        );

        return false;
    }

}
