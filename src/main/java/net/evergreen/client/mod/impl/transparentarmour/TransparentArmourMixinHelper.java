/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mod.impl.transparentarmour;

import net.evergreen.client.Evergreen;

public class TransparentArmourMixinHelper {

    private static TransparentArmour mod;

    public static TransparentArmour getMod() {
        if (mod == null)
            mod = Evergreen.getInstance().getModManager().getMod(TransparentArmour.class);
        return mod;
    }

}
