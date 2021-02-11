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

@Getter
@SuppressWarnings({"BusyWait"})
public class Musician extends Thread {

    private final Director director;

    private Queue<Papers> queuePapers, mirrorQueuePapers;
    private final Instrument instrument;
    @Setter
    private int checkEvery;
    @Setter
    private boolean debugMode;
    private boolean stopSignal;
    @Deprecated
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
    }

    private void commonInit() {
        this.checkEvery = 250;
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

    private void clearQueues() {
        for (Papers paper : mirrorQueuePapers) {
            queuePapers.remove(paper);
        }
        mirrorQueuePapers.clear();
    }

    @Deprecated
    public void play() {
        running = true;
    }

    public void announceEnd() {
        stopSignal = true;
    }


    @Override
    public void run() {
        try {
            int papersWritten = 0;
            long startTime, getConnectionTime = 0;

            while (!stopSignal) {

                if (queuePapers.isEmpty()) {
                    Thread.sleep(checkEvery);
                    continue;
                }
                startTime = System.currentTimeMillis();
                updateMirror(queuePapers);

                Connection connection = null;
                try {
                    connection = instrument.getDataSource().getConnection();
                    getConnectionTime = System.currentTimeMillis();

                    for (Papers paper : mirrorQueuePapers) {
                        paper.writePaper(connection, instrument);
                        papersWritten++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    instrument.close(connection);
                    clearQueues();

                    if (debugMode && getConnectionTime != 0) {
                        System.out.println("[database-lib] Worker: " + currentThread().getName());
                        System.out.println("[database-lib] Got connection in: " + (getConnectionTime - startTime) + "ms");
                        System.out.println("[database-lib] Wrote " + papersWritten + " paper(s) in "
                                        + (System.currentTimeMillis() - startTime) + " ms");
                    }
                    papersWritten = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
