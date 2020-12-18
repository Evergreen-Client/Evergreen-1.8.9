/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mod.impl.transparentarmour;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.NumberSetting;

public class TransparentArmour extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Transparent Armour", "Allows you to make armour render with an opacity.", ModMeta.Category.GRAPHIC, null);
    }

    public NumberSetting opacity;

    @Override
    public void initialise() {
        addSetting(opacity = new NumberSetting(100, 0, 100, "Armour Opacity", "Opacity percentage for armour.", NumberSetting.StoreType.INTEGER, "", "%"));
    }
}
