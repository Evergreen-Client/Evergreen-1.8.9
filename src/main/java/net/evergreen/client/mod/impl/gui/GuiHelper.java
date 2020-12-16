/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (menuKeybind.isPressed()) {
            Evergreen.getInstance().getGuiHandler().open(new MainScreen());
        }
    }
}
