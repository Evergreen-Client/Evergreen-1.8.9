/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.anticheat.impl;

import net.evergreen.client.Evergreen;
import net.evergreen.client.anticheat.Check;
import net.evergreen.client.event.EventEntityAttackEntity;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.utils.EntityUtils;
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
