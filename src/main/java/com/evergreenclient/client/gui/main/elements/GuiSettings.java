/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui.main.elements;

import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.setting.Setting;
import com.evergreenclient.client.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class GuiSettings extends Gui {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private final Mod mod;
    private int x, y, width, height;
    private Color listBGColor;
    private Color listFGColor;
    private Color listPressedColor;

    private Setting hoveredSetting = null;

    public GuiSettings(Mod modIn) {
        this.mod = modIn;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        int count = 1;
        Setting hovered = null;
        for (Setting s : mod.getSettings()) {
            int top = y + (count * height);
            int bottom = top + height;
            boolean pressed = mouseX >= x && mouseX <= x + width && mouseY >= top && mouseY <= bottom;
            if (pressed)
                hovered = s;
            drawRect(x, top, x + width, bottom, (pressed ? listPressedColor.getRGB() : listBGColor.getRGB()));
            String name = StringUtils.trimStringToWidth(s.getDisplayName(), mc.fontRendererObj, width);
            drawString(mc.fontRendererObj, name, x + 2, top + (height / 2) - (mc.fontRendererObj.FONT_HEIGHT / 2), listFGColor.getRGB());
            count++;
        }
        hoveredSetting = hovered;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getListBGColor() {
        return listBGColor;
    }

    public void setListBGColor(Color listBGColor) {
        this.listBGColor = listBGColor;
    }

    public Color getListFGColor() {
        return listFGColor;
    }

    public void setListFGColor(Color listFGColor) {
        this.listFGColor = listFGColor;
    }

    public Color getListPressedColor() {
        return listPressedColor;
    }

    public void setListPressedColor(Color listPressedColor) {
        this.listPressedColor = listPressedColor;
    }
}
