/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.anticheat.impl;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.anticheat.Check;
import com.evergreenclient.client.event.EventEntityAttackEntity;
import com.evergreenclient.client.utils.EntityUtils;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Checks if reach distance is too large
 *
 * @author isXander
 */
public class ReachCheck extends Check {

    public ReachCheck() {
        super("Reach");
        Evergreen.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttackEntity(EventEntityAttackEntity event) {
        if (event.attacker instanceof EntityPlayerSP) {
            double dist = EntityUtils.getReachDistanceFromEntity(event.victim);
            if (dist > (mc.thePlayer.capabilities.isCreativeMode ? 6.0 : 3.0) || dist == -1) {
                logSuspicion(String.format("Distance: %s. Ping: %s", dist, mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId()).getResponseTime()));
                event.setCancelled(true);
            }
        }
    }

}
