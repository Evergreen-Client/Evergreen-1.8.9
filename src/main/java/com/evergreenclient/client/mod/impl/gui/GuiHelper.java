/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.gui;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.gui.main.impl.MainScreen;
import com.evergreenclient.client.injection.ClientRegistry;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class GuiHelper extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Gui Helper", "This is a backend modification. If you see this in any way, report it to Evergreen.", ModMeta.Category.OTHER, null);
    }

    @Override
    public boolean backendMod() {
        return true;
    }

    public KeyBinding menuKeybind;

    @Override
    public void initialise() {
        ClientRegistry.registerKeyBind(menuKeybind = new KeyBinding("Menu", Keyboard.KEY_RSHIFT, "Evergreen"));
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (menuKeybind.isPressed()) {
            Evergreen.getInstance().getGuiHandler().open(new MainScreen());
        }
    }
}
