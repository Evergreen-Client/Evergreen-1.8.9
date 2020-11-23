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

package net.evergreen.client;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import net.evergreen.client.event.EventModInitialization;
import net.evergreen.client.gui.GuiHandler;
import net.evergreen.client.mod.ModManager;
import net.evergreen.client.utils.ReflectionCache;
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
    private GuiHandler guiHandler;
    private ReflectionCache reflectionCache;

    private Evergreen() {
        Evergreen.instance = this;
        EventBus.INSTANCE.register(this);
    }

    public static Evergreen createInstance() {
        instance = new Evergreen();
        return getInstance();
    }

    public static Evergreen getInstance() {
        return instance;
    }

    @InvokeEvent
    private void preInit(EventModInitialization.Pre event) {
        if (!dataDir.exists())
            dataDir.mkdirs();

        this.reflectionCache = new ReflectionCache();
        this.modManager = new ModManager();
        this.modManager.registerMods();
        this.guiHandler = new GuiHandler();
    }

    @InvokeEvent(priority = Priority.HIGH)
    private void init(EventModInitialization.Post event) {
        this.modManager.loadModSettings();
        this.modManager.initialiseMods();
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

}
