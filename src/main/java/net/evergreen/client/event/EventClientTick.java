package net.evergreen.client.event;

import cc.hyperium.event.Event;
import cc.hyperium.event.Phase;

public class EventClientTick extends Event {

    public final Phase phase;

    public EventClientTick(Phase phase) {
        this.phase = phase;
    }

}
