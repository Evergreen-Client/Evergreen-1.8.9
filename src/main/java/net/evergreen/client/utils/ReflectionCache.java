/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

import java.lang.reflect.Field;

public class ReflectionCache {

    public Timer timer;
    public Session session;

    public ReflectionCache() {
        getCache();
    }

    public void refreshCache() {
        getCache();
    }

    private void getCache() {
        try {
            timer = (Timer) getField("timer", "field_71428_T").get(Minecraft.getMinecraft());
            session = (Session) getField("session", "field_71449_j").get(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getField(String mcpName, String srgName) {
        Field f = null;
        try {
            f = Minecraft.class.getDeclaredField(mcpName);
        }
        catch (NoSuchFieldException e) {
            try {
                f = Minecraft.class.getDeclaredField(srgName);
            }
            catch (NoSuchFieldException e2) {
                e.printStackTrace();
            }
        }
        if (f != null) {
            f.setAccessible(true);
            return f;
        }
        return null;
    }

}
