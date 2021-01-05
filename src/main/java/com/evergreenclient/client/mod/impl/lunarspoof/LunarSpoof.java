/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.lunarspoof;

import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;

public class LunarSpoof extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("LunarSpoof", "Makes servers believe you are on Lunar Client.", ModMeta.Category.OTHER, null);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

}
