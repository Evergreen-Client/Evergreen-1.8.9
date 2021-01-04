/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.gui.mainmenu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiEvergreenMainMenu extends GuiScreen {

    private static final ResourceLocation ICON = new ResourceLocation("evergreen/gui/mainmenu/icon.png");

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, new Color(24, 28, 37).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableAlpha();
        ScaledResolution res = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(ICON);
        drawScaledCustomSizeModalRect((res.getScaledWidth() / 2) - (100 / 2), 0, 0, 0, 2000, 2000, 100, 100, 2000, 2000);
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
