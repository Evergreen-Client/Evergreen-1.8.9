/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.event;

import com.evergreenclient.client.event.bus.Event;
import com.evergreenclient.client.event.bus.Phase;

public class EventRenderTick extends Event {

    public final Phase phase;
    public final float partialTicks;

    public EventRenderTick(Phase phase, float partialTicks) {
        this.partialTicks = partialTicks;
        this.phase = phase;
    }

}
