package net.herospvp.database;

import net.herospvp.database.items.Notes;

public class Main {

    public static void main(String[] args) {
        String[] strings = {"username CHAR(16) NOT NULL", "kills INTEGER UNSIGNED", "deaths INTEGER UNSIGNED",
                "streak INTEGER UNSIGNED", "noDeaths INTEGER UNSIGNED", "noPings INTEGER UNSIGNED",
                "noMsg INTEGER UNSIGNED", "PRIMARY KEY(username)"};

        /*
        "CREATE TABLE IF NOT EXISTS " + table +
                    " (username CHAR(16) NOT NULL, kills INTEGER UNSIGNED, deaths INTEGER UNSIGNED," +
                    " streak INTEGER UNSIGNED, noDeaths INTEGER UNSIGNED, noPings INTEGER UNSIGNED," +
                    " noMsg INTEGER UNSIGNED, PRIMARY KEY(username));"
         */
        System.out.println(new Notes("combo").createTable(strings));
    }

}
