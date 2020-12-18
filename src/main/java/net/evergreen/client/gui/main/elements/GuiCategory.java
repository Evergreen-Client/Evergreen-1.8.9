/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.gui.main.elements;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCategory extends Gui {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private final String title;
    private final List<Mod> mods;
    private int x, y, width, height;
    private Color titleBGColor;
    private Color titleFGColor;
    private Color listBGColor;
    private Color listFGColor;
    private Color listPressedColor;
    private Color listEnabledColor;

    private Mod hoveredMod = null;

    private boolean mouseDown;
    private int xOff;
    private int yOff;

    public GuiCategory(String title) {
        this.title = title;
        this.mods = new ArrayList<>();
    }

    public void addModules(Mod... mods) {
        this.mods.addAll(Arrays.asList(mods));
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(float partialTicks, int mouseX, int mouseY) {
        drawRect(x, y, x + width, y + height, titleBGColor.getRGB());
        drawCenteredString(mc.fontRendererObj, title, x + (width / 2), y + (height / 2) - (mc.fontRendererObj.FONT_HEIGHT / 2), titleFGColor.getRGB());
        int count = 1;
        Mod hovered = null;
        for (Mod m : mods) {
            int top = y + (count * height);
            int bottom = top + height;
            boolean pressed = mouseX >= x && mouseX <= x + width && mouseY >= top && mouseY <= bottom;
            if (pressed)
                hovered = m;
            drawRect(x, top, x + width, bottom, (pressed ? listPressedColor.getRGB() : listBGColor.getRGB()));
            String name = StringUtils.trimStringToWidth(m.getMetadata().getName(), mc.fontRendererObj, width);
            drawString(mc.fontRendererObj, name, x + 2, top + (height / 2) - (mc.fontRendererObj.FONT_HEIGHT / 2), (m.isEnabled() ? listEnabledColor.getRGB() : listFGColor.getRGB()));
            count++;
        }
        hoveredMod = hovered;
    }

    public void handleClick(int mouseX, int mouseY, int mouseButton) {
        if (hoveredMod != null) {
            hoveredMod.toggle();
        }
    }

    public void handleMouseInput(int mouseX, int mouseY, int mouseButton) {
        if (mouseDown) {
            x = mouseX - xOff;
            y = mouseY - yOff;
        }
    }

    public void startDragging(int mouseX, int mouseY) {
        mouseDown = true;
        xOff = mouseX - x;
        yOff = mouseY - y;
    }

    public void stopDragging() {
        mouseDown = false;
        xOff = yOff = 0;
    }

    public String getTitle() {
        return title;
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

    public Color getTitleBGColor() {
        return titleBGColor;
    }

    public void setTitleBGColor(Color titleBGColor) {
        this.titleBGColor = titleBGColor;
    }

    public Color getTitleFGColor() {
        return titleFGColor;
    }

    public void setTitleFGColor(Color titleFGColor) {
        this.titleFGColor = titleFGColor;
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

    public Color getListEnabledColor() {
        return listEnabledColor;
    }

    public void setListEnabledColor(Color listEnabledColor) {
        this.listEnabledColor = listEnabledColor;
    }

    public Mod getHoveredMod() {
        return hoveredMod;
    }
}
