/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.gui.main.impl;

import net.evergreen.client.Evergreen;
import net.evergreen.client.gui.main.GuiBase;
import net.evergreen.client.gui.main.elements.GuiCategory;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;

import java.awt.*;

public class MainScreen extends GuiBase {

    @Override
    public void initGui() {
        super.initGui();
        int count = 0;
        for (ModMeta.Category c : ModMeta.Category.values()) {
            GuiCategory guiC = new GuiCategory(c.displayName);
            guiC.addModules(Evergreen.getInstance().getModManager().getMods(c)
                    .stream().filter(m -> !m.backendMod()).toArray(Mod[]::new));
            guiC.setHeight(20);
            guiC.setWidth(100);
            guiC.setLocation((count * 20) + ((count + 1) * 100), 20);
            guiC.setTitleBGColor(new Color(255, 0, 0));
            guiC.setTitleFGColor(new Color(255, 255, 255));
            guiC.setListBGColor(new Color(0, 0, 0, 100));
            guiC.setListFGColor(new Color(170, 170, 170));
            guiC.setListPressedColor(new Color(255, 255, 255, 100));
            guiC.setListEnabledColor(new Color(255, 255, 255));
            lists.add(guiC);
            count++;
        }
    }

    @Override
    public void onGuiClosed() {
        Evergreen.getInstance().getModManager().saveModSettings();
    }
}
