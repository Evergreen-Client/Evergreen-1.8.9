/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mod.impl.extracontrols;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.injection.ClientRegistry;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ExtraControls extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Extra Controls", "Adds extra controls to MC to make your experience better.", ModMeta.Category.QOL, null);
    }
    @Override
    public boolean backendMod() {
        return true;
    }

    public KeyBinding dropStack;

    @Override
    public void initialise() {
        ClientRegistry.registerKeyBind(dropStack = new KeyBinding("Drop Stack", Keyboard.KEY_LMENU, "Evergreen"));
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (dropStack.isPressed()) {
            mc.thePlayer.dropOneItem(true);
        }
    }


}
