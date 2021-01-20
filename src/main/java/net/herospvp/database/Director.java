package net.herospvp.database;

import lombok.Getter;
import net.herospvp.database.items.Instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Director {

    @Getter
    private final Map<String, Instrument> instrumentsCollection;
    @Getter
    private final Collection<Musician> musicians;

    public Director() {
        this.instrumentsCollection = new HashMap<>();
        this.musicians = new ArrayList<>();
        System.out.println("[database-lib] Currently using version: 1.1.0-SNAPSHOT");
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

    public void addMusician(Musician musician) {
        musicians.add(musician);
    }

    public void endShow() {
        for (Musician musician : musicians)
            musician.announceEnd();
    }

}
