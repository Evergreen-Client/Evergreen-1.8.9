/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.simplestats;

import net.evergreen.client.command.ClientCommandHandler;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.Setting;
import net.evergreen.client.setting.SettingField;

public class SimpleStats extends Mod {

    @SettingField(
            type = Setting.PropertyType.TEXT,
            name = "API Key",
            description = "This mod uses your hypixel api key to function."
    )
    public String apiKey = "";

    @Override
    public void initialise() {
        ClientCommandHandler.instance.registerCommand(new StatsCommand());
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @Override
    public ModMeta getMetadata() {
        return new ModMeta(
                "SimpleStats",
                "No BS Hypixel stats mod",
                "Nora",
                ModMeta.Category.QOL,
                null
        );
    }
}
