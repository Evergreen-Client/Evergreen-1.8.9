/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

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

    public static void drawCenteredString(FontRenderer fr, String text, float x, float y, int color, boolean shadow) {
        fr.drawString(text, x - (fr.getStringWidth(text) / 2f), y, color, shadow);
    }

}
