/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class NewGuiMainMenu extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, new Color(0, 0, 0).getRGB());
        float worldX = 5;
        float worldY = 7;
        float worldZ = 5;
        float f = width / (float)Math.tan(70);
        float g = height / (float)Math.tan(70);
        int screenX = (int)((worldX * f) / worldZ) + (width / 2);
        int screenY = (int)((worldY * g) / worldZ) + (height / 2);
        drawRect(screenX, screenY, screenX + 5, screenY + 5, new Color(255, 255, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
