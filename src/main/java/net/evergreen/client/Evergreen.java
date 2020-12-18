/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client;

import net.evergreen.client.anticheat.AntiCheatManager;
import net.evergreen.client.discord.EvergreenRPC;
import net.evergreen.client.event.EventGuiOpen;
import net.evergreen.client.event.bus.EventBus;
import net.evergreen.client.event.bus.Priority;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.gui.GuiHandler;
import net.evergreen.client.gui.screens.resourcepack.GuiResourcePacks;
import net.evergreen.client.mod.ModManager;
import net.evergreen.client.utils.ReflectionCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;

/**
 * Main class of Evergreen
 * Initialise things here
 */
@SuppressWarnings({ "unused", "ResultOfMethodCallIgnored" })
public class Evergreen {

    public static final Logger logger = LogManager.getLogger("Evergreen");
    public static final File dataDir = new File(Minecraft.getMinecraft().mcDataDir, "Evergreen");
    public static final EventBus EVENT_BUS = new EventBus();
    public static final String VERSION = "1.0";

    private static Evergreen instance;
    private static boolean running = false;

    private ModManager modManager;
    private GuiHandler guiHandler;
    private ReflectionCache reflectionCache;
    private EvergreenRPC evergreenRPC;
    private AntiCheatManager antiCheatManager;

    private Evergreen() {
        EVENT_BUS.register(this);
    }

    public static Evergreen getInstance() {
        if (instance == null)
            instance = new Evergreen();
        return instance;
    }

    public void preInit() {
        running = true;

        if (!dataDir.exists())
            dataDir.mkdirs();

        this.reflectionCache = new ReflectionCache();
        this.modManager = new ModManager();
        this.modManager.injectExternalMods();
        this.modManager.registerMods();
        this.guiHandler = new GuiHandler();
        this.evergreenRPC = new EvergreenRPC();
        this.antiCheatManager = new AntiCheatManager();
    }

    public void init() {
        this.antiCheatManager.start();
        this.modManager.loadModSettings();
        this.evergreenRPC.init();
        this.modManager.initialiseMods();
    }

    /**
     * Used to exit any loops safely, a bit like minecraft does
     *
     * @author isXander
     */
    public static void shutdown() {
        running = false;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public GuiHandler getGuiHandler() {
        return guiHandler;
    }

    public ReflectionCache getReflectionCache() {
        return reflectionCache;
    }

    public AntiCheatManager getAntiCheatManager() {
        return antiCheatManager;
    }

    public EvergreenRPC getEvergreenRPC() {
        return evergreenRPC;
    }

    public static boolean isRunning() {
        return running;
    }

    @SubscribeEvent(priority = Priority.LOW)
    public void onGuiOpen(EventGuiOpen event) {
        if (event.screen != null && event.screen.getClass() == GuiScreenResourcePacks.class) {
            event.setCancelled(true);
        }
    }

}
