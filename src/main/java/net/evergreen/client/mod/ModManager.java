/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientShutdown;
import net.evergreen.client.event.EventRenderGameOverlay;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.utils.JarLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager {

    private final List<Mod> mods = new CopyOnWriteArrayList<>();

    public ModManager() {
        Evergreen.EVENT_BUS.register(this);
    }

    /**
     * Looks through all classes in mod package for objects
     * that extend {@link Mod} and register them
     *
     * @author isXander
     */
    public void registerMods() {
        // Easier than registering each mod manually
        Reflections r1 = new Reflections("net.evergreen.client.mod.impl");
        for (Class<? extends Mod> m : r1.getSubTypesOf(Mod.class)) {
            try {
                Mod mod = m.newInstance();
                mod.setHudMod(m.getAnnotation(HUD.class) != null);
                addMod(mod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Repeat for external mods...
        Reflections r2 = new Reflections("evergreen");
        try {
            for (Class<? extends Mod> m : r2.getSubTypesOf(Mod.class)) {
                try {
                    addMod(m.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (ReflectionsException e) {
        }
    }

    private void addMod(Mod mod) {
        if (!mods.contains(mod))
            mods.add(mod);
    }

    public void initialiseMods() {
        for (Mod mod : mods) {
            mod.initialise();
            if (mod.backendMod())
                mod.setEnabled(true);
        }
    }

    public void loadModSettings() {
        for (Mod mod : mods) {
            mod.loadSettings();
        }
    }

    public void saveModSettings() {
        for (Mod mod : mods) {
            mod.saveSettings();
        }
    }

    /**
     * Gets mod from registered list using class
     *
     * @param clazz class of mod
     * @return instance of mod that has been registered
     * @author isXander
     */
    public <T extends Mod> T getMod(Class<? extends T> clazz) {
        return (T)mods.stream().filter(m -> m.getClass().getName().equals(clazz.getName())).findAny().orElse(null);
    }

    public List<Mod> getMods() {
        return mods;
    }

    /**
     * Gets all mods with certain category
     *
     * @param category category to filter
     * @return list of mods with certain category
     * @author isXander
     */
    public List<Mod> getMods(ModMeta.Category category) {
        List<Mod> mods = new ArrayList<>();
        for (Mod m : getMods()) {
            if (m.getMetadata().getCategory() == category)
                mods.add(m);
        }
        return mods;
    }

    /**
     * Injects jar files in mod folder into classpath
     * so {@link ModManager} can find and register mod
     * classes to load.
     *
     * @author isXander
     */
    public void injectExternalMods() {
        File modFolder = new File(Evergreen.dataDir, "mods");
        if (modFolder.exists()) {
            for (File f : modFolder.listFiles()) {
                if (f.isFile()) {
                    if (FilenameUtils.getExtension(f.getName()).equalsIgnoreCase("jar")) {
                        try {
                            JarLoader.addToClasspath(f);
                        }
                        catch (UnsupportedClassVersionError e) {
                            throw new ReportedException(CrashReport.makeCrashReport(e, "Could not load mod " + f.getName()
                                    + "\nThis is because the mod was built with a newer JDK"
                                    + "\nTo fix this, make sure you are building your project with Java 8"));
                        }
                    }
                }
            }
        } else {
            modFolder.mkdirs();
        }
    }

    @SubscribeEvent
    public void onShutdown(EventClientShutdown event) {
        saveModSettings();
    }

    @SubscribeEvent
    public void onRenderOverlay(EventRenderGameOverlay event) {
        Minecraft mc = Minecraft.getMinecraft();
        for (Mod m : mods) {
            if (mc.currentScreen == null && mc.inGameHasFocus)
                m.render(event);
        }
    }

}
