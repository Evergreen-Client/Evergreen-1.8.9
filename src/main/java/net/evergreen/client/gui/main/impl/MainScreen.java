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
            guiC.setListFGColor(new Color(255, 255, 255));
            guiC.setListPressedColor(new Color(255, 255, 255, 100));
            guiC.setListEnabledColor(new Color(170, 170, 170));
            lists.add(guiC);
            count++;
        }
    }
}
