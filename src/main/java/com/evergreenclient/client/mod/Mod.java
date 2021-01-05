/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod;

import com.evergreenclient.client.event.EventRenderGameOverlay;
import com.evergreenclient.client.setting.Setting;
import com.evergreenclient.client.setting.SettingArray;
import com.evergreenclient.client.setting.SettingField;
import com.evergreenclient.client.utils.json.BetterJsonObject;
import com.evergreenclient.client.Evergreen;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * This should be in the constructor but a parameter
     * in it would need every mod to add a constructor
     *
     * @param state new hud state
     * @author isXander
     */
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
     * @author isXander
     */
    protected final void addSetting(Setting<?>... settings) {
        Collections.addAll(this.settings, settings);
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

    /**
     * Gets JSON object from file and passes it to parsing function
     *
     * @author isXander
     */
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

    /**
     * Adds all settings to json object and other things such as positioning
     *
     * @author isXander
     */
    public final void saveSettings() {
        Evergreen.logger.info("Saving " + getMetadata().getName() + " Mod.");
        try {
            if (!configFile.getParentFile().exists())
                configFile.getParentFile().mkdirs();

            if (!configFile.exists() && !configFile.createNewFile()) {
                Evergreen.logger.error("Failed to save " + getMetadata().getName() + " Mod -- something went very wrong.");
                throw new ReportedException(CrashReport.makeCrashReport(new Throwable(), "Could not save " + getMetadata().getName() + " Mod configuration. Cannot continue."));
            }

            BetterJsonObject rootObject = new BetterJsonObject();
            BetterJsonObject settingsObject = new BetterJsonObject();

            rootObject.addProperty("enabled", enabled);
            if (hud) {
                rootObject.addProperty("x", x);
                rootObject.addProperty("y", y);
                rootObject.addProperty("scale", scale);
            }

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
            }
            rootObject.add("settings", settingsObject);
            rootObject.writeToFile(configFile);
        }
        catch (IOException e) {
            Evergreen.logger.error("Failed to save " + getMetadata().getName() + " Mod -- something went very wrong.");
            throw new ReportedException(CrashReport.makeCrashReport(new Throwable(), "Could not save " + getMetadata().getName() + " Mod configuration. Cannot continue."));
        }
    }

    /**
     * Converts JSON to all the settings and positioning stuff
     *
     * @param o object to be parsed
     * @author isXander
     */
    @SuppressWarnings("unchecked")
    private void parseSettings(BetterJsonObject o) {
        // Nested for loops are bad and there is probably a miles better way to do this.
        // Loops through every key, finds the setting with the name of the key and parses accordingly
        for (String key : o.getAllKeys()) {
            switch (key) {
                case "enabled":
                    setEnabled(o.optBoolean(key));
                    break;
                case "x":
                    x = o.optInt(key);
                    break;
                case "y":
                    y = o.optInt(key);
                    break;
                case "scale":
                    scale = (float) o.optDouble(key);
                    break;
                case "settings":
                    for (String k : new BetterJsonObject(o.get(key).getAsJsonObject()).getAllKeys()) {
                        for (Setting<?> s : settings) {
                            if (s.getJsonKeyName().equals(k)) {
                                switch (s.type()) {
                                    case TEXT:
                                        ((Setting<String>) s).set(o.optString(k));
                                        break;
                                    case ARRAY:
                                        ((Setting<SettingArray>) s).get().setIndex(o.optInt(k));
                                        break;
                                    case COLOR:
                                        ((Setting<Color>) s).set(new Color(o.optInt(k)));
                                        break;
                                    case FLOAT:
                                        ((Setting<Float>) s).set(new Double(o.optDouble(k)).floatValue());
                                        break;
                                    case BOOLEAN:
                                        ((Setting<Boolean>) s).set(o.optBoolean(k));
                                        break;
                                    case INTEGER:
                                        ((Setting<Integer>) s).set(o.optInt(k));
                                        break;
                                }
                            }
                        }
                    }
                    break;
            }

        }
    }

    /**
     * Used for bunch of stuff
     * Can be used in mod events for when they aren't enabled
     *
     * @return is enabled
     * @author isXander
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of the mod
     *
     * @param state new state
     * @author isXander
     */
    public void setEnabled(boolean state) {
        enabled = state;
        if (state)
            onEnabled();
        else
            onDisabled();
    }

    /**
     * Used when guis need to toggle
     */
    public void toggle() {
        setEnabled(!isEnabled());
    }

    /**
     * For mods to use as an event
     *
     * @author isXander
     */
    protected void onEnabled() {

    }

    /**
     * For mods to use as an event
     *
     * @author isXander
     */
    protected void onDisabled() {

    }

}
