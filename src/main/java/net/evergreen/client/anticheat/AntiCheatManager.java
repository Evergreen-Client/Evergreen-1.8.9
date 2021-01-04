/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
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
