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

package net.evergreen.client.gui.main.elements;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.setting.Setting;
import net.evergreen.client.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class GuiSettings extends Gui {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private final Mod mod;
    private int x, y, width, height;
    private Color listBGColor;
    private Color listFGColor;
    private Color listPressedColor;

    private Setting hoveredSetting = null;

    public GuiSettings(Mod modIn) {
        this.mod = modIn;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        int count = 1;
        Setting hovered = null;
        for (Setting s : mod.getSettings()) {
            int top = y + (count * height);
            int bottom = top + height;
            boolean pressed = mouseX >= x && mouseX <= x + width && mouseY >= top && mouseY <= bottom;
            if (pressed)
                hovered = s;
            drawRect(x, top, x + width, bottom, (pressed ? listPressedColor.getRGB() : listBGColor.getRGB()));
            String name = StringUtils.trimStringToWidth(s.getDisplayName(), mc.fontRendererObj, width);
            drawString(mc.fontRendererObj, name, x + 2, top + (height / 2) - (mc.fontRendererObj.FONT_HEIGHT / 2), listFGColor.getRGB());
            count++;
        }
        hoveredSetting = hovered;
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
}
