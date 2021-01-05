/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.transparentarmour;

import com.evergreenclient.client.Evergreen;

public class TransparentArmourMixinHelper {

    private static TransparentArmour mod;

    public static TransparentArmour getMod() {
        if (mod == null)
            mod = Evergreen.getInstance().getModManager().getMod(TransparentArmour.class);
        return mod;
    }

}
