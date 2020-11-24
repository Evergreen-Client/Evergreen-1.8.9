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
