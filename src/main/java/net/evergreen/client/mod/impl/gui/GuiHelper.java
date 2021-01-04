/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.gui;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.gui.GuiHandler;
import net.evergreen.client.gui.main.impl.MainScreen;
import net.evergreen.client.injection.ClientRegistry;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
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
