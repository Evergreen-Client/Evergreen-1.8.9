/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.gui.mainmenu;

import net.evergreen.client.gui.elements.GuiButtonExt;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiEvergreenMainMenu extends GuiScreen {

    private static final ResourceLocation ICON = new ResourceLocation("evergreen/gui/mainmenu/icon.png");

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButtonExt(0, width / 2 - 1 - 20, height - 20, 20, 20, "S"));
        this.buttonList.add(new GuiButtonExt(1, width / 2 + 1, height - 20, 20, 20, "M"));
        this.buttonList.add(new GuiButtonExt(2, width - 20, 0, 20, 20, "X"));
        this.buttonList.add(new GuiButtonExt(3, 0, 0, 20, 20, "O"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, new Color(24, 28, 37).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableAlpha();
        ScaledResolution res = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(ICON);
        drawScaledCustomSizeModalRect((res.getScaledWidth() / 2) - (100 / 2), 40, 0, 0, 2000, 2000, 100, 100, 2000, 2000);
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                mc.shutdown();
                break;
            case 3:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
        }
        super.actionPerformed(button);
    }
}
