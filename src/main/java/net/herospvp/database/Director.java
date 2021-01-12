package net.herospvp.database;

import lombok.Getter;
import net.herospvp.database.items.Instrument;
import net.herospvp.database.items.Papers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Director {

    @Getter
    private final Map<String, Instrument> instrumentsCollection;
    @Getter
    private final Queue<Papers> papersQueue;

    public Director() {
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

}
