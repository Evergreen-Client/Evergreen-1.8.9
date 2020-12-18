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

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (dropStack.isPressed()) {
            mc.thePlayer.dropOneItem(true);
        }
    }


}
