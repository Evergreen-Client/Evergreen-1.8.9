/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.fullbright;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientShutdown;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;

public class FullBright extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Fullbright", "Makes minecraft brighter", ModMeta.Category.GRAPHIC, null);
    }

    @Override
    public void initialise() {
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @Override
    protected void onEnabled() {
        System.out.println("enabled");
        mc.gameSettings.gammaSetting = 15;
    }

    @Override
    protected void onDisabled() {
        System.out.println("disabled");
        mc.gameSettings.gammaSetting = 1;
    }

    @SubscribeEvent
    public void onShutdown(EventClientShutdown event) {
        mc.gameSettings.gammaSetting = 1;
    }

}
