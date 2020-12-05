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

package net.evergreen.client.anticheat;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.utils.ServerUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.reflections.Reflections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiCheatManager {

    private final List<Check> checks = new CopyOnWriteArrayList<>();

    public void start() {
        registerChecks();
        Evergreen.EVENT_BUS.register(this);
    }

    private void registerChecks() {
        Reflections reflections = new Reflections("net.evergreen.client.anticheat.impl");
        for (Class<? extends Check> m : reflections.getSubTypesOf(Check.class)) {
            try {
                addCheck(m.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addCheck(Check check) {
        if (!checks.contains(check))
            checks.add(check);
    }

}
