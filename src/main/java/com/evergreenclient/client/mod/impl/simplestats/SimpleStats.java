/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.simplestats;

import com.evergreenclient.client.command.ClientCommandHandler;
import com.evergreenclient.client.setting.SettingField;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.setting.Setting;

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
