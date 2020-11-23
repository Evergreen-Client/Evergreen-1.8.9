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

package net.evergreen.client.mod;

import net.evergreen.client.Evergreen;
import net.evergreen.client.setting.*;
import net.evergreen.client.utils.json.BetterJsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Mod {

    protected final Minecraft mc;
    protected boolean enabled;
    private final File configFile;
    protected final Random random = new Random();

    protected final List<Setting> settings;

    public Mod() {
        this.settings = new ArrayList<>();
        this.mc = Minecraft.getMinecraft();
        this.enabled = false;
        this.configFile = new File(new File(Evergreen.dataDir, "modules"), getMetadata().getName() + ".json");
        this.initialise();
        this.loadSettings();
    }

    public final List<Setting> getSettings() {
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
     * Adds setting to internal list
     *
     * @param settings settings to add
     */
    protected final void addSetting(Setting... settings) {
        for (Setting s : settings) {
            if (!s.getJsonKeyName().startsWith("_")) {
                this.settings.add(s);
            }
            else {
                Evergreen.logger.warn("Settings that begin with \"_\" are not allowed, skipping.");
            }
        }
    }

    public void loadSettings() {
        Evergreen.logger.info("Loading " + getMetadata().getName() + " Mod.");
        try {
            parseSettings(BetterJsonObject.getFromFile(configFile));
        }
        catch (IOException e) {
            Evergreen.logger.warn("Failed to load " + getMetadata().getName() + " Mod, defaulting settings.");
            saveSettings();
        }
    }

    public void saveSettings() {
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
            for (Setting s : settings) {
                if (s instanceof BooleanSetting)
                    settingsObject.addProperty(s.getJsonKeyName(), ((BooleanSetting) s).getValue());
                else if (s instanceof NumberSetting)
                    settingsObject.addProperty(s.getJsonKeyName(), ((NumberSetting) s).getValue());
                else if (s instanceof ColorSetting)
                    settingsObject.addProperty(s.getJsonKeyName(), ((ColorSetting) s).getColor().getRGB());
                else
                    Evergreen.logger.warn("Generic or unknown setting detected, ignoring.");
            }

            settingsObject.writeToFile(configFile);
        }
        catch (IOException e) {
            Evergreen.logger.error("Failed to save " + getMetadata().getName() + " Mod -- something went very wrong.");
            throw new ReportedException(CrashReport.makeCrashReport(new Throwable(), "Could not save " + getMetadata().getName() + " Mod configuration. Cannot continue."));
        }
    }

    private void parseSettings(BetterJsonObject o) {
        // Nested for loops are bad and there is probably a miles better way to do this.
        // Loops through every key, finds the setting with the name of the key and parses accordingly
        for (String key : o.getAllKeys()) {
            for (Setting s : settings) {
                if (key.equals("_enabled"))
                    enabled = o.optBoolean("_enabled");
                else if (s.getJsonKeyName().equals(key)) {
                    if (s instanceof BooleanSetting)
                        ((BooleanSetting) s).setValue(o.optBoolean(key));
                    else if (s instanceof NumberSetting) {
                        NumberSetting setting = (NumberSetting) s;

                        switch (setting.getType()) {
                            case INTEGER:
                                setting.setValue(o.optInt(key));
                                break;
                            case FLOAT:
                            case DOUBLE:
                                setting.setValue(o.optDouble(key));
                                break;
                        }
                    }
                    else if (s instanceof ColorSetting)
                        ((ColorSetting) s).setColor(new Color(o.optInt(key)));
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
    }

    public void toggle() {
        enabled = !enabled;
    }

}
