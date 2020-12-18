/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.utils;

import net.minecraft.client.gui.FontRenderer;

public class StringUtils {

    public static String trimStringToWidth(String str, FontRenderer fr, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (fr.getStringWidth(sb.toString()) > width) {
            sb.replace(sb.length() - 5, sb.length(), "...");
        }
        return sb.toString();
    }

}
