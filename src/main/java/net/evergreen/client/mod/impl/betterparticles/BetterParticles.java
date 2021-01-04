/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.betterparticles;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.Setting;
import net.evergreen.client.setting.SettingField;

public class BetterParticles extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Better Particles", "Improves particle rendering to look much cleaner.", ModMeta.Category.GRAPHIC, null);
    }

    @SettingField(
            type = Setting.PropertyType.INTEGER,
            name = "Opacity Speed",
            description = "Changes how fast particles become transparent.",
            max = 5
    )
    public Integer alphaSpeed = 3;

    @Override
    protected Mod getSelf() {
        return this;
    }
}
