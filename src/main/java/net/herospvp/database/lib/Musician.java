package net.herospvp.database.lib;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.herospvp.database.Main;
import net.herospvp.database.lib.items.Instrument;
import net.herospvp.database.lib.items.Papers;

import java.awt.print.Paper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Queue;
import java.util.concurrent.*;

@SuppressWarnings({"BusyWait", "unused"})
@Getter
public class Musician extends Thread {

    private Main instance;

    private BlockingQueue<Papers> blockingQueue;
    private Instrument instrument;
    private final boolean debug;
    private boolean running;

    public Musician(Instrument instrument) {
        this.debug = false;
        commonInit(instrument);
    }

    public Musician(Instrument instrument, boolean debug) {
        this.debug = debug;
        commonInit(instrument);
    }

    private void commonInit(Instrument instrument) {
        this.blockingQueue = new LinkedBlockingQueue<>();
        this.instrument = instrument;
        this.running = true;

        currentThread().setName("M-#" + Main.getPlugin(Main.class).getDirector());

        this.start();
    }

    public void offer(Papers papers) {
        blockingQueue.offer(papers);
    }

    @SneakyThrows
    @Override
    public void run() {

        int i = 0;
        long time;

        while (running) {

            if (blockingQueue.isEmpty()) {
                Thread.sleep(100);
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
                System.out.println("[database-lib] Worker: " + currentThread().getName());
                System.out.println("[database-lib] Wrote " + i + " paper(s) in "
                        + (System.currentTimeMillis() - time) + " ms");
            }

            i = 0;
        }

    }

}
