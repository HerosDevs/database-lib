package net.herospvp.database.lib;

import lombok.Getter;
import net.herospvp.database.Main;
import net.herospvp.database.lib.items.Instrument;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
@Getter
public class Director {

    private final Map<String, Instrument> instrumentsCollection;
    private final Collection<Musician> musicians;

    public Director() {
        this.instrumentsCollection = new HashMap<>();
        this.musicians = new ArrayList<>();
    }

    public void addInstrument(
            @NotNull String name,
            @NotNull Instrument instrument
    ) {
        instrumentsCollection.put(name, instrument);
    }

    public void removeInstrument(
            @NotNull String name
    ) {
        instrumentsCollection.remove(name);
    }

    public Instrument getInstrument(
            @NotNull String name
    ) {
        return instrumentsCollection.get(name);
    }

    public void addMusician(
            @NotNull Musician... array
    ) {
        Arrays.stream(array).iterator().forEachRemaining(musicians::add);
    }

}
