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

package net.evergreen.client.gui.screens.resourcepack.utils;

import net.evergreen.client.gui.screens.resourcepack.GuiResourcePacks;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public class ResourcePackListEntryFolder extends ResourcePackListEntryCustom {

    private static final ResourceLocation folderResource = new ResourceLocation("evergreen", "gui/resourcepack/folder.png"); // http://www.iconspedia.com/icon/folion-icon-27237.html

    private final GuiResourcePacks ownerScreen;

    public final File folder;
    public final String folderName;
    public final boolean isUp;

    public ResourcePackListEntryFolder(GuiResourcePacks ownerScreen, File folder){
        super(ownerScreen);
        this.ownerScreen = ownerScreen;
        this.folder = folder;
        this.folderName = folder.getName();
        this.isUp = false;
    }

    public ResourcePackListEntryFolder(GuiResourcePacks ownerScreen, File folder, boolean isUp){
        super(ownerScreen);
        this.ownerScreen = ownerScreen;
        this.folder = folder;
        this.folderName = "..";
        this.isUp = isUp;
    }

    @Override
    public void func_148313_c(){
        mc.getTextureManager().bindTexture(folderResource);
    }

    @Override
    public String func_148312_b(){
        return folderName;
    }

    @Override
    public String func_148311_a(){
        return isUp ? "(Back)" : "(Folder)";
    }

    @Override
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_){
        ownerScreen.moveToFolder(folder);
        return true;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected){
        GuiResourcePacks.renderFolderEntry(this,x,y,isSelected);
    }

}
