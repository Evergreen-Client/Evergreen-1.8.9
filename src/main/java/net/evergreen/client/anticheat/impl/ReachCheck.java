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
