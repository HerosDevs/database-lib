package net.herospvp.database.lib;

import lombok.Getter;
import lombok.SneakyThrows;
import net.herospvp.database.DatabaseLib;
import net.herospvp.database.lib.items.Instrument;
import net.herospvp.database.lib.items.Papers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.concurrent.*;

@SuppressWarnings({"BusyWait", "unused"})
@Getter
public class Musician extends Thread {

    private DatabaseLib instance;
    private final Director director;

    private BlockingQueue<Papers> blockingQueue;
    private Instrument instrument;
    private final boolean debug;
    private boolean running;
    private final int timeToWait;

    public Musician(
            @NotNull Director director,
            @NotNull Instrument instrument
    ) {
        this.debug = false;
        this.timeToWait = 100;
        this.director = director;
        commonInit(instrument);
    }

    public Musician(
            @NotNull Director director,
            @NotNull Instrument instrument,
            int timeToWait
    ) {
        this.debug = false;
        this.timeToWait = timeToWait;
        this.director = director;
        commonInit(instrument);
    }

    public Musician(
            @NotNull Director director,
            @NotNull Instrument instrument,
            boolean debug
    ) {
        this.debug = debug;
        this.timeToWait = 100;
        this.director = director;
        commonInit(instrument);
    }

    public Musician(
            @NotNull Director director,
            @NotNull Instrument instrument,
            int timeToWait,
            boolean debug
    ) {
        this.debug = false;
        this.timeToWait = timeToWait;
        this.director = director;
        commonInit(instrument);
    }

    private void commonInit(
            @NotNull Instrument instrument
    ) {
        this.blockingQueue = new LinkedBlockingQueue<>();
        this.instrument = instrument;
        this.running = true;

        this.setName("M-#" + director.getMusicians().size());
        director.addMusician(this);

        this.start();
    }

    public void offer(
            @Nullable Papers papers
    ) {
        if (papers == null) return;
        blockingQueue.offer(papers);
    }

    @SneakyThrows
    @Override
    public void run() {

        int i = 0;
        long time;

        while (running) {

            if (blockingQueue.isEmpty()) {
                Thread.sleep(timeToWait);
                continue;
            }

            time = System.currentTimeMillis();

            Connection connection = null;
            try {
                connection = instrument.getDataSource().getConnection();

                Papers papers;
                while ((papers = blockingQueue.poll()) != null) {
                    papers.writePaper(connection, instrument);
                    i++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                instrument.close(connection);
            }

            if (debug) {
                System.out.println("[database-lib] Worker: " + this.getName());
                System.out.println("[database-lib] Wrote " + i + " paper(s) in "
                        + (System.currentTimeMillis() - time) + " ms");
            }

            i = 0;
        }

    }

}
