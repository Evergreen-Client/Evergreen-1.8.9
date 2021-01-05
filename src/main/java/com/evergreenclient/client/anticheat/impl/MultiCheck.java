/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.anticheat.impl;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.anticheat.Check;
import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.EventEntityAttackEntity;
import com.evergreenclient.client.event.bus.Phase;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;

public class MultiCheck extends Check {

    private int count = 0;

    public MultiCheck() {
        super("Multi Aura");
        Evergreen.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (event.phase == Phase.PRE) return;

        if (count < 0)
            logSuspicion("Count variable has been tampered with. Illegal modifications have been made to the client.");
        if (count > 1)
            logSuspicion("Multiple attacks per tick which is impossible.");

        count = 0;
    }

    @SubscribeEvent
    public void onAttack(EventEntityAttackEntity event) {
        if (event.attacker instanceof EntityPlayerSP)
            count++;
    }

}
