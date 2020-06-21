package io.github.brendoncurmi.fusionpixelmon.api.ui;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventHandler {

    private HashMap<Event, BiConsumer<Object, AbstractPlayer>> listeners = new HashMap<>();

    public void add(Event event, BiConsumer<Object, AbstractPlayer> listener) {
        listeners.put(event, listener);
    }

    public boolean has(Event event) {
        return listeners.containsKey(event);
    }

    public BiConsumer<Object, AbstractPlayer> get(Event event) {
        return listeners.get(event);
    }

    public void call(Event eventType, Object event, AbstractPlayer player) {
        if (has(eventType)) {
            get(eventType).accept(event, player);
        }
    }
}
