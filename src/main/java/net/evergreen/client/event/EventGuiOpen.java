/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.CancellableEvent;
import net.minecraft.client.gui.GuiScreen;

public class EventGuiOpen extends CancellableEvent {

    public GuiScreen screen;

    public EventGuiOpen(GuiScreen screen) {
        this.screen = screen;
    }

}
