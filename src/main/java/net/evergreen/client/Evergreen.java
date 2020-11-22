package net.evergreen.client;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import net.evergreen.client.event.EventModInitialization;
import net.evergreen.client.mod.ModManager;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Main class of Evergreen
 * Initialise things here
 */
public class Evergreen {

    public static final Logger logger = LogManager.getLogger("Evergreen");
    public static final File dataDir = new File(Minecraft.getMinecraft().mcDataDir, "Evergreen");
    public static final String VERSION = "1.0";

    private static Evergreen instance;

    private ModManager modManager;

    public Evergreen() {
        Evergreen.instance = this;
        EventBus.INSTANCE.register(this);
    }

    public static Evergreen getInstance() {
        return instance;
    }

    @InvokeEvent
    private void preInit(EventModInitialization.Pre event) {
        this.modManager = new ModManager();
        this.modManager.registerMods();
        this.modManager.initialiseMods();
    }

    public ModManager getModManager() {
        return modManager;
    }

}
