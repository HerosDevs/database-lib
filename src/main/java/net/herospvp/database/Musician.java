package net.herospvp.database;

import lombok.Getter;
import net.herospvp.database.items.Instrument;
import net.herospvp.database.items.Papers;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Musician {

    @Getter
    private final Map<String, Instrument> instrumentsCollection;
    @Getter
    private final Queue<Papers> papersQueue;

    public Musician() {
        this.instrumentsCollection = new HashMap<>();
        this.papersQueue = new LinkedList<>();
    }

    public void addInstrument(String name, Instrument instrument) {
        instrumentsCollection.put(name, instrument);
    }

    public void removeInstrument(String name) {
        instrumentsCollection.remove(name);
    }

    public Instrument getInstrument(String name) {
        return instrumentsCollection.get(name);
    }

    public static class Play extends Thread {

        @Getter
        private Queue<Papers> mirrorQueuePapers;
        @Getter
        private static boolean isRunning;
        @Getter
        private final Instrument instrument;

        public Play(Instrument instrument) {
            this.instrument = instrument;
            this.mirrorQueuePapers = new LinkedList<>();
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

        @Override
        public void run() {
            isRunning = true;
            System.out.println("JOB ON " + currentThread().getName() + " STARTED!");

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

            System.out.println("JOB ON " + currentThread().getName() + " COMPLETED!");
            isRunning = false;
        }

    }

}
