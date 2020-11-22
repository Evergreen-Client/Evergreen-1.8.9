package net.evergreen.client.event;

import cc.hyperium.event.Event;
import cc.hyperium.event.Phase;

public class EventRenderTick extends Event {

    public final Phase phase;
    public final float partialTicks;

    public EventRenderTick(Phase phase, float partialTicks) {
        this.partialTicks = partialTicks;
        this.phase = phase;
    }

}
