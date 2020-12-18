/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
