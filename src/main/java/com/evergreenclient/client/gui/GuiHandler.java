/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui;

import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.bus.Phase;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.Evergreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiHandler {

    private GuiScreen screen = null;

    public GuiHandler() {
        Evergreen.EVENT_BUS.register(this);
    }

    public void open(GuiScreen screen) {
        this.screen = screen;
    }

    @SubscribeEvent
    public void tick(EventClientTick event) {
        if (event.phase == Phase.PRE) {
            if (screen != null) {
                Minecraft.getMinecraft().displayGuiScreen(screen);
                screen = null;
            }
        }
    }

}
