package net.herospvp.database;

import lombok.SneakyThrows;

public class Main {

    private static final Musician musician = new Musician("a");

    @SneakyThrows
    public static void main(String[] args) {

        musician.play();

        Thread.sleep(1000);

    }

}
