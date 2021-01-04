/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.lunarspoof;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;

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
