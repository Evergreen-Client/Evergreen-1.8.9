/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventRenderGameOverlay;
import net.evergreen.client.setting.*;
import net.evergreen.client.utils.json.BetterJsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Mod {

    protected final Minecraft mc;
    protected boolean enabled;
    private final File configFile;
    protected final Random random = new Random();

    protected final List<Setting<?>> settings;

    /* For hud mods */
    private boolean hud;
    protected int x = 0;
    protected int y = 0;
    protected float scale = 1;

    public Mod() {
        this.settings = new ArrayList<>();
        this.mc = Minecraft.getMinecraft();
        this.enabled = false;
        this.configFile = new File(new File(Evergreen.dataDir, "mods"), getMetadata().getName() + ".json");
        discoverSettings();
    }

    public void setHudMod(boolean state) {
        this.hud = state;
    }

    public final List<Setting<?>> getSettings() {
        return settings;
    }

    /**
     * Used for GUIs, nicer than putting stuff in constructors
     * and the fact you can store all the info in an object.
     *
     * @return metadata for gui
     */
    public abstract ModMeta getMetadata();

    /**
     * Tells Mod Manager if this mod should be always on and hidden in GUIs
     * @author isXander
     */
    public boolean backendMod() {
        return false;
    }

    /**
     * Making it as forge-compatible as possible
     * so we can easily port mods.
     *
     * Register keybindings, settings etc here
     *
     * @author isXander
     */
    public void initialise() {

    }

    /**
     * Needed to discover settings in child class
     * There is probably a better way to do this
     * but for now this is the workaround, will
     * be changed in the future if StackOverflow
     * question is answered
     *
     * @author isXander
     * @return instance of child class
     */
    protected abstract Mod getSelf();

    /**
     * Used for HUD mods, whether or not the mod will render or not
     * is up to Evergreen itself and not the mod.
     *
     * @author isXander
     */
    public void render(EventRenderGameOverlay event) {

    }

    /**
     * Adds setting to internal list
     *
     * @param settings settings to add
     */
    protected final void addSetting(Setting<?>... settings) {
        for (Setting<?> s : settings) {
            if (!s.getJsonKeyName().startsWith("_")) {
                this.settings.add(s);
            }
            else {
                Evergreen.logger.warn("Settings that begin with \"_\" are not allowed, skipping.");
            }
        }
    }

    /**
     * Discovers annotated fields in child class and
     * adds them to settings accordingly
     *
     * @author isXander
     */
    private void discoverSettings() {
        System.out.println("Discovering settings for " + getMetadata().getName());
        for (Field f : getSelf().getClass().getDeclaredFields()) {
            System.out.println("    Found field: " + f.getName());
            SettingField annotation = f.getAnnotation(SettingField.class);
            if (annotation != null) {
                System.out.println("        Found annotation");
                f.setAccessible(true);
                try {
                    // FIXME: 19/12/2020 f.get(getSelf()); is null, find out why
                    switch (annotation.type()) {
                        case TEXT:
                            System.out.println("            Is type of text");
                            addSetting(new Setting<>((String) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.prefix(), annotation.suffix()));
                            break;
                        case ARRAY:
                            System.out.println("            Is type of array");
                            addSetting(new Setting<>((SettingArray) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.prefix(), annotation.suffix()));
                            break;
                        case COLOR:
                            System.out.println("            Is type of color");
                            addSetting(new Setting<>((Color) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.prefix(), annotation.suffix()));
                            break;
                        case FLOAT:
                            System.out.println("            Is type of float");
                            addSetting(new Setting<>((Float) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.min(), annotation.max(), annotation.prefix(), annotation.suffix()));
                            break;
                        case BOOLEAN:
                            System.out.println("            Is type of bool");
                            addSetting(new Setting<>((Boolean) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.prefix(), annotation.suffix()));
                            break;
                        case INTEGER:
                            System.out.println("            Is type of int");
                            addSetting(new Setting<>((Integer) f.get(getSelf()), annotation.type(), annotation.name(), annotation.description(), annotation.min(), annotation.max(), annotation.prefix(), annotation.suffix()));
                            break;
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final void loadSettings() {
        Evergreen.logger.info("Loading " + getMetadata().getName() + " Mod.");
        try {
            parseSettings(BetterJsonObject.getFromFile(configFile));
        }
        catch (IOException e) {
            Evergreen.logger.warn("Failed to load " + getMetadata().getName() + " Mod, defaulting settings.");
            saveSettings();
        }
    }

    public final void saveSettings() {
        Evergreen.logger.info("Saving " + getMetadata().getName() + " Mod.");
        try {
            if (!configFile.getParentFile().exists())
                configFile.getParentFile().mkdirs();

            if (!configFile.exists() && !configFile.createNewFile()) {
                Evergreen.logger.error("Failed to save " + getMetadata().getName() + " Mod -- something went very wrong.");
                throw new ReportedException(CrashReport.makeCrashReport(new Throwable(), "Could not save " + getMetadata().getName() + " Mod configuration. Cannot continue."));
            }

            BetterJsonObject settingsObject = new BetterJsonObject();
            settingsObject.addProperty("_enabled", enabled);
            for (Setting<?> s : settings) {
                System.out.println(s);
                switch (s.type()) {
                    case INTEGER:
                        settingsObject.addProperty(s.getJsonKeyName(), (Integer) s.get());
                        break;
                    case BOOLEAN:
                        settingsObject.addProperty(s.getJsonKeyName(), (Boolean) s.get());
                        break;
                    case FLOAT:
                        settingsObject.addProperty(s.getJsonKeyName(), (Float) s.get());
                        break;
                    case COLOR:
                        settingsObject.addProperty(s.getJsonKeyName(), ((Color)s.get()).getRGB());
                        break;
                    case ARRAY:
                        settingsObject.addProperty(s.getJsonKeyName(), ((SettingArray) s.get()).getIndex());
                        break;
                    case TEXT:
                        settingsObject.addProperty(s.getJsonKeyName(), (String) s.get());
                        break;
                }
//                if (s.get() instanceof Boolean)
//                    settingsObject.addProperty(s.getJsonKeyName(), (Boolean) s.get());
//                else if (s.get() instanceof Float)
//                    settingsObject.addProperty(s.getJsonKeyName(), (Float) s.get());
//                else if (s.get() instanceof Color)
//                    settingsObject.addProperty(s.getJsonKeyName(), ((Color)s.get()).getRGB());
//                else if (s.get() instanceof Integer)
//                    settingsObject.addProperty(s.getJsonKeyName(), (Integer) s.get());
//                else if (s.get() instanceof SettingArray)
//                    settingsObject.addProperty(s.getJsonKeyName(), ((SettingArray) s.get()).getIndex());
//                else if (s.get() instanceof String)
//                    settingsObject.addProperty(s.getJsonKeyName(), (String) s.get());
//                else
//                    Evergreen.logger.warn("Generic or unknown setting detected, ignoring.");
            }

            settingsObject.writeToFile(configFile);
        }
        catch (IOException e) {
            Evergreen.logger.error("Failed to save " + getMetadata().getName() + " Mod -- something went very wrong.");
            throw new ReportedException(CrashReport.makeCrashReport(new Throwable(), "Could not save " + getMetadata().getName() + " Mod configuration. Cannot continue."));
        }
    }

    @SuppressWarnings("unchecked")
    private void parseSettings(BetterJsonObject o) {
        // Nested for loops are bad and there is probably a miles better way to do this.
        // Loops through every key, finds the setting with the name of the key and parses accordingly
        for (String key : o.getAllKeys()) {
            for (Setting<?> s : settings) {
                if (key.equals("_enabled"))
                    setEnabled(o.optBoolean("_enabled"));
                else if (s.getJsonKeyName().equals(key)) {
                    if (s.get() instanceof Boolean)
                        ((Setting<Boolean>)s).set(o.optBoolean(key));
                    else if (s.get() instanceof Double)
                        ((Setting<Double>)s).set(o.optDouble(key));
                    else if (s.get() instanceof Color)
                        ((Setting<Color>) s).set(new Color(o.optInt(key)));
                    else if (s.get() instanceof String)
                        ((Setting<String>)s).set(o.optString(key));
                    else if (s.get() instanceof Float)
                        ((Setting<Float>)s).set(new Double(o.optDouble(key)).floatValue());
                    else if (s.get() instanceof Integer)
                        ((Setting<Integer>)s).set(o.optInt(key));
                    else if (s.get() instanceof SettingArray)
                        ((Setting<SettingArray>)s).get().setIndex(o.optInt(key));
                    else
                        Evergreen.logger.warn("Generic or unknown setting detected, ignoring.");
                }
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        enabled = state;
        if (state)
            onEnabled();
        else
            onDisabled();
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    protected void onEnabled() {

    }

    protected void onDisabled() {

    }

}
