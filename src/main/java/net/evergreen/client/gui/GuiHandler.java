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

package net.evergreen.client.gui;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.bus.Phase;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.bus.SubscribeEvent;
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
