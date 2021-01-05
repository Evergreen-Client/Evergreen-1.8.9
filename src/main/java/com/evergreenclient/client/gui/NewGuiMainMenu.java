/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui;

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
