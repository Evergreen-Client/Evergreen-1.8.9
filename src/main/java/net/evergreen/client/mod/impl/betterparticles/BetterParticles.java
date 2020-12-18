/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mod.impl.betterparticles;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.NumberSetting;

public class BetterParticles extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Better Particles", "Improves particle rendering to look much cleaner.", ModMeta.Category.GRAPHIC, null);
    }

    public NumberSetting alphaSpeed;

    @Override
    public void initialise() {
        addSetting(alphaSpeed = new NumberSetting(3, 0, 5, "Opacity Speed", "Changes how fast particles become transparent.", NumberSetting.StoreType.INTEGER, "", ""));
    }
}
