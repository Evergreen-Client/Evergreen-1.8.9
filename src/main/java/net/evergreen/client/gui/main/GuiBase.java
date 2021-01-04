/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.gui.main;

import net.evergreen.client.gui.main.elements.GuiCategory;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiBase extends GuiScreen {

    protected List<GuiCategory> lists = new ArrayList<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (GuiCategory list : lists) {
            list.draw(partialTicks, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (GuiCategory list : lists) {
                list.handleClick(mouseX, mouseY, mouseButton);
            }
            for (GuiCategory list : lists) {
                if (mouseX >= list.getX() && mouseX <= list.getX() + list.getWidth() && mouseY >= list.getY() && mouseY <= list.getY() + list.getHeight()) {
                    lists.remove(list);
                    lists.add(0, list);
                    list.startDragging(mouseX, mouseY);
                    break;
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (GuiCategory list : lists) {
            list.stopDragging();
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (GuiCategory list : lists) {
            list.handleMouseInput(mouseX, mouseY, clickedMouseButton);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
