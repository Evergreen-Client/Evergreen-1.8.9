/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.reachdisplay;

import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.EventRenderGameOverlay;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventEntityAttackEntity;
import com.evergreenclient.client.mod.HUD;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.utils.EntityUtils;
import com.evergreenclient.client.utils.StringUtils;
import net.minecraft.client.entity.EntityPlayerSP;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@HUD
public class ReachDisplay extends Mod {

    private double reach = 0;
    private long lastHit = 0L;

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Reach Display", "Shows the distance you reached to an enemy.", ModMeta.Category.GRAPHIC, null);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @Override
    public void initialise() {
        Evergreen.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttackEntity(EventEntityAttackEntity event) {
        if (event.attacker instanceof EntityPlayerSP) {
            reach = EntityUtils.getReachDistanceFromEntity(event.victim);
            lastHit = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (System.currentTimeMillis() - lastHit > 3000)
            reach = 0;
    }

    @Override
    public void render(EventRenderGameOverlay event) {
        StringUtils.drawCenteredString(mc.fontRendererObj, "[Reach: " + getFormattedReach() + "]", x, y, -1, true);
    }

    private String getFormattedReach() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(reach);
    }
}
