package net.herospvp.database;

import lombok.Getter;
import lombok.Setter;
import net.herospvp.database.items.Instrument;
import net.herospvp.database.items.Papers;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Musician extends Thread {

    @Getter
    private Queue<Papers> mirrorQueuePapers;
    @Getter
    private boolean running = false;
    @Getter
    private Instrument instrument;

    public Musician(Instrument instrument) {
        this.instrument = instrument;
        this.mirrorQueuePapers = new LinkedList<>();
        this.start();
    }

    @Deprecated
    public Musician(String string) {
        this.start();
    }

    public void updateMirror(Papers papers) {
        this.mirrorQueuePapers.add(papers);
    }

    public void updateMirror(Queue<Papers> mirrorQueuePapers) {
        this.mirrorQueuePapers = mirrorQueuePapers;
    }

    public void clearMirror() {
        this.mirrorQueuePapers.clear();
    }

    public void play() {
        running = !running;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!running) {
                    Thread.sleep(100);
                    continue;
                }
                System.out.println("[database-lib] JOB ON " + currentThread().getName() + " STARTED!");

                Connection connection = null;
                try {

                    connection = instrument.getDataSource().getConnection();

                    for (Papers paper : mirrorQueuePapers) {
                        paper.writePaper(connection, instrument);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    instrument.close(connection, null, null);
                }
                clearMirror();

                System.out.println("[database-lib] JOB ON " + currentThread().getName() + " COMPLETED!");
                running = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
