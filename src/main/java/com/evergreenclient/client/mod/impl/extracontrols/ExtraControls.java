/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.extracontrols;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.injection.ClientRegistry;
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
