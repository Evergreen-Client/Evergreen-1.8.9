/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.Event;
import net.evergreen.client.event.bus.Phase;

public class EventClientTick extends Event {

    public final Phase phase;

    public EventClientTick(Phase phase) {
        this.phase = phase;
    }

}
