/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui;

import com.evergreenclient.client.event.EventRenderTick;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.utils.BooleanUtils;
import com.evergreenclient.client.utils.MathUtils;
import com.evergreenclient.client.Evergreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationToast extends Gui {

    public static final NotificationToast INSTANCE = new NotificationToast();

    private static final long notificationTimeMS = 5000;
    private static final float notificationSpeed = 0.000001f;
    private long lastNotif;
    private ScaledResolution res;
    private int prevLeft;
    private int prevRight;
    /* If false moving out, else move in */
    private boolean state;

    private final Queue<String> notifications;

    public NotificationToast() {
        this.lastNotif = 0;
        this.res = new ScaledResolution(Minecraft.getMinecraft());
        this.prevLeft = this.prevRight = res.getScaledWidth() / 2;
        this.notifications = new ConcurrentLinkedQueue<>();
        this.state = false;
        Evergreen.EVENT_BUS.register(this);
    }

    public void notify(String message) {
        notifications.add(message);
    }

    @SubscribeEvent
    public void onRender(EventRenderTick event) {
        if (notifications.size() == 0) return;

        state = System.currentTimeMillis() - lastNotif > notificationTimeMS / 2 && !state;

        int desiredLeft;
        int desiredRight;
        if (state) {
            desiredLeft = desiredRight = res.getScaledWidth() / 2;
        }
        else {
            desiredLeft =  (res.getScaledWidth() / 2) - (res.getScaledWidth() / 4);
            desiredRight = (res.getScaledWidth() / 2) + (res.getScaledWidth() / 4);
        }

        if (!BooleanUtils.noneNull(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld)) return;

        GlStateManager.pushMatrix();
        int left =  prevLeft  = (int) MathUtils.lerp(prevLeft,  desiredLeft, event.partialTicks * notificationSpeed);
        int right = prevRight = (int) MathUtils.lerp(prevRight, desiredRight,event.partialTicks * notificationSpeed);
        drawRect(left, res.getScaledHeight() / 2, right, (res.getScaledHeight() / 2) + 15, new Color(0, 0, 0, 200).getRGB());
        //GL11.glScissor(left, 0, right - left, 1000);
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, notifications.element(), res.getScaledWidth() / 2, (res.getScaledHeight() / 2) + (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2), -1);
        GlStateManager.popMatrix();

        if (System.currentTimeMillis() - lastNotif > notificationTimeMS) {
            state = false;
            notifications.remove();
            if (notifications.size() != 0)
                lastNotif = System.currentTimeMillis();
        }
    }

}
