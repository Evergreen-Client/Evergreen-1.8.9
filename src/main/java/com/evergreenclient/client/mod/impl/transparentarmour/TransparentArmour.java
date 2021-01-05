/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.transparentarmour;

import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.setting.Setting;
import com.evergreenclient.client.setting.SettingField;

public class TransparentArmour extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Transparent Armour", "Allows you to make armour render with an opacity.", ModMeta.Category.GRAPHIC, null);
    }

    @SettingField(
            type = Setting.PropertyType.INTEGER,
            name = "Armour Opacity",
            description = "Opacity percentage for armour.",
            suffix = "%"
    )
    public Integer opacity = 100;

    @Override
    protected Mod getSelf() {
        return this;
    }
}
