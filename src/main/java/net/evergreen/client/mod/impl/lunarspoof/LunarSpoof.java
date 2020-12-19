/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
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
