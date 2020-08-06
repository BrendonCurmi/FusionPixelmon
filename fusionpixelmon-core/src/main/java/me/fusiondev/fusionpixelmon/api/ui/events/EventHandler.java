package me.fusiondev.fusionpixelmon.api.ui.events;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;

import java.util.HashMap;
import java.util.function.BiConsumer;

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
