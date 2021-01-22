package net.herospvp.database;

import lombok.Getter;
import lombok.Setter;
import net.herospvp.database.items.Instrument;
import net.herospvp.database.items.Papers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings({"BusyWait"})
public class Musician extends Thread {

    private final Director director;

    @Getter
    private Queue<Papers> queuePapers, mirrorQueuePapers;
    @Getter
    private final Instrument instrument;
    @Getter
    @Setter
    private int checkEvery;
    @Getter
    @Setter
    private boolean debugMode;
    private boolean stopSignal;
    @Getter
    private boolean running;

    public Musician(Director director, Instrument instrument) {
        this.director = director;
        this.instrument = instrument;

        commonInit();
    }

    public Musician(Director director, Instrument instrument, boolean debugMode) {
        this.director = director;
        this.instrument = instrument;
        this.debugMode = debugMode;

        commonInit();

        if (debugMode)
            System.out.println("[database-lib] Musician created! (" + 1000 / checkEvery + "/s)");
    }

    private void commonInit() {
        this.checkEvery = 500;
        this.queuePapers = new LinkedList<>();
        this.mirrorQueuePapers = new LinkedList<>();
        this.start();
        director.addMusician(this);
    }


    public void update(@Nullable Papers papers) {
        if (papers == null) return;
        queuePapers.add(papers);
    }

    public void update(@NotNull Queue<Papers> queuePapers) {
        for (Papers paper : queuePapers)
            update(paper);
    }


    private void updateMirror(@Nullable Papers papers) {
        if (papers == null) return;
        mirrorQueuePapers.add(papers);
    }

    private void updateMirror(@NotNull Queue<Papers> queuePapers) {
        for (Papers paper : queuePapers)
            updateMirror(paper);
    }

    private void clearMirror() {
        mirrorQueuePapers.clear();
    }


    public void play() {
        running = true;
    }

    public void announceEnd() {
        stopSignal = true;
    }


    @Override
    public void run() {
        try {
            long time = 0;

            while (!stopSignal) {

                if (!running) {
                    Thread.sleep(checkEvery);
                    continue;
                }

                if (debugMode) {
                    time = System.currentTimeMillis();
                    System.out.println("[database-lib] JOB ON " + currentThread().getName() + " STARTED!");
                }

                updateMirror(queuePapers);

                Connection connection = null;
                try {
                    connection = instrument.getDataSource().getConnection();

                    for (Papers paper : mirrorQueuePapers)
                        paper.writePaper(connection, instrument);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    instrument.close(connection, null, null);
                    running = false;
                }
                clearMirror();

                if (debugMode)
                    System.out.println("[database-lib] JOB ON " + currentThread().getName() + " COMPLETED! (" +
                            + (System.currentTimeMillis() - time) / 1000.0 + "s)");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
