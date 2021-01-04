/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.borderlesswindowed;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.bus.Phase;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.minecraft.client.Minecraft;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class BorderlessWindowed extends Mod {

    private boolean lastFullscreen;

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Borderless Windowed", "Makes fullscreen function better.", ModMeta.Category.QOL, null);
    }

    @Override
    public void initialise() {
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @Override
    protected void onEnabled() {
        fix(Minecraft.getMinecraft().isFullScreen());
    }

    @Override
    protected void onDisabled() {
        setMode(Minecraft.getMinecraft().isFullScreen());
    }

    @SubscribeEvent
    public void onTick(EventClientTick event) {
        final boolean fullScreenNow = Minecraft.getMinecraft().isFullScreen();

        if (event.phase != Phase.POST) {
            return;
        }

        if (!isEnabled()) {
            if (Minecraft.getMinecraft().isFullScreen())
                setMode(true);

            return;
        }
        if (this.lastFullscreen != fullScreenNow) {
            this.fix(fullScreenNow);
            this.lastFullscreen = fullScreenNow;
        }
    }

    public void setMode(boolean mode) {
        if (!mode)
            fix(false);
        else {
            try {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setFullscreen(true);
                Display.setResizable(false);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    public void fix(boolean fullscreen) {
        try {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
                Display.setResizable(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
                Display.setResizable(true);
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
