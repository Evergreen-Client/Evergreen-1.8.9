/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.gui.mainmenu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiEvergreenMainMenu extends GuiScreen {

    private static final ResourceLocation ICON = new ResourceLocation("evergreen/gui/mainmenu/icon.png");

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        ScaledResolution res = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(ICON);
        drawScaledCustomSizeModalRect(0, 0, 0, 0, 2000, 2000, 100, 100, 2000, 2000);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
