/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui.screens.resourcepack;

import com.evergreenclient.client.gui.screens.resourcepack.utils.RenderPackListOverlay;
import com.evergreenclient.client.gui.screens.resourcepack.utils.ResourcePackListEntryFolder;
import com.evergreenclient.client.gui.screens.resourcepack.utils.ResourcePackListProcessor;
import com.evergreenclient.client.gui.screens.resourcepack.utils.ResourcePackRepositoryCustom;
import com.evergreenclient.client.utils.ReflectionHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraft.util.Util;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuiResourcePacks extends GuiScreenResourcePacks {

    private final GuiScreen parentScreen;

    private GuiTextField searchField;
    private GuiResourcePackAvailable guiPacksAvailable;
    private GuiResourcePackSelected guiPacksSelected;
    private List<ResourcePackListEntry> listPacksAvailable, listPacksAvailableProcessed, listPacksDummy;
    private List<ResourcePackListEntry> listPacksSelected;
    private ResourcePackListProcessor listProcessor;

    private File currentFolder;
    private GuiButton selectedButton;
    private boolean hasUpdated, requiresReload;

    private Comparator<ResourcePackListEntry> currentSorter;

    public GuiResourcePacks(GuiScreen parentScreenIn) {
        super(parentScreenIn);
        this.parentScreen = parentScreenIn;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        buttonList.add(new GuiOptionButton(1, width / 2 + 100 - 75, height - 26, I18n.format("gui.done")));
        buttonList.add(new GuiOptionButton(2, width / 2 + 100 - 75, height - 48, I18n.format("resourcePack.openFolder")));

        buttonList.add(new GuiOptionButton(10, width / 2 - 204, height - 26, 40, 20, "A-Z"));
        buttonList.add(new GuiOptionButton(11, width / 2 - 204 + 44, height - 26, 40, 20, "Z-A"));
        buttonList.add(new GuiOptionButton(20, width / 2 - 74, height - 26, 70, 20, "Refresh"));

        String prevText = searchField == null ? "" : searchField.getText();
        searchField = new GuiTextField(30, mc.fontRendererObj, width / 2 - 203, height - 46, 198, 16);
        searchField.setText(prevText);

        if (!requiresReload) {
            listPacksAvailable = listPacksAvailableProcessed = listPacksSelected = Lists.newArrayListWithCapacity(8);
            listPacksDummy = Lists.newArrayListWithCapacity(1);

            ResourcePackRepository repository = mc.getResourcePackRepository();
            repository.updateRepositoryEntriesAll();

            currentFolder = repository.getDirResourcepacks();
            listPacksAvailable.addAll(createAvailablePackList(repository));

            for (Entry entry : Lists.reverse(repository.getRepositoryEntries()))
                listPacksSelected.add(new ResourcePackListEntryFound(this, entry));

            listPacksSelected.add(new ResourcePackListEntryDefault(this));
        }

        guiPacksAvailable = new GuiResourcePackAvailable(mc, 200, height, listPacksAvailableProcessed);
        guiPacksAvailable.setSlotXBoundsFromLeft(width / 2 - 204);
        guiPacksAvailable.registerScrollButtons(7, 8);
        ReflectionHelper.setPrivateValue(GuiResourcePackAvailable.class, guiPacksAvailable, 4);

        guiPacksSelected = new GuiResourcePackSelected(mc, 200, height, listPacksAvailableProcessed);
        guiPacksSelected.setSlotXBoundsFromLeft(width / 2 + 4);
        guiPacksSelected.registerScrollButtons(7, 8);
        ReflectionHelper.setPrivateValue(GuiResourcePackSelected.class, guiPacksSelected, 4);

        listProcessor = new ResourcePackListProcessor(listPacksAvailable, listPacksAvailableProcessed);
        listProcessor.setSorter(currentSorter == null ? (currentSorter = ResourcePackListProcessor.sortAZ) : currentSorter);
        listProcessor.setFilter(searchField.getText().trim());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 20:
                refreshAvailablePacks();
                break;
            case 11:
                listProcessor.setSorter(currentSorter = ResourcePackListProcessor.sortZA);
                break;
            case 10:
                listProcessor.setSorter(currentSorter = ResourcePackListProcessor.sortAZ);
                break;
            case 2:
                openFolder(mc.getResourcePackRepository().getDirResourcepacks());
                break;
            case 1:
                if (requiresReload) {
                    List<Entry> selected = refreshSelectedPacks();
                    mc.gameSettings.resourcePacks.clear();
                    for (Entry entry : selected)
                        mc.gameSettings.resourcePacks.add(entry.getResourcePackName());

                    mc.gameSettings.saveOptions();
                    mc.refreshResources();

                    RenderPackListOverlay.refreshPackNames();
                }

                mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (GuiButton b : buttonList) {
                if (b.mousePressed(mc, mouseX, mouseY)) {
                    selectedButton = b;
                    b.playPressSound(mc.getSoundHandler());
                    actionPerformed(b);
                }
            }
        }

        guiPacksAvailable.mouseClicked(mouseX, mouseY, mouseButton);
        guiPacksSelected.mouseClicked(mouseX, mouseY, mouseButton);
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

        listProcessor.refresh();
    }

    @Override
    public void handleMouseInput() throws IOException {
        try {
            super.handleMouseInput();
        } catch (NullPointerException e) {
        }

        guiPacksAvailable.handleMouseInput();
        guiPacksSelected.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0 && selectedButton != null) {
            selectedButton.mouseReleased(mouseX, mouseY);
            selectedButton = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (searchField.isFocused()) {
            searchField.textboxKeyTyped(typedChar, keyCode);
            listProcessor.setFilter(searchField.getText().trim());
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        searchField.updateCursorCounter();

        if (hasUpdated) {
            hasUpdated = false;
            refreshSelectedPacks();
            refreshAvailablePacks();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        guiPacksAvailable.drawScreen(mouseX, mouseY, partialTicks);
        guiPacksSelected.drawScreen(mouseX, mouseY, partialTicks);
        searchField.drawTextBox();

        for (GuiButton b : buttonList) {
            b.drawButton(mc, mouseX, mouseY);
        }
    }

    public void moveToFolder(File folder){
        currentFolder = folder;
        refreshSelectedPacks();
        refreshAvailablePacks();
    }

    public void refreshAvailablePacks(){
        listPacksAvailable.clear();
        listPacksAvailable.addAll(createAvailablePackList(mc.getResourcePackRepository()));
        listProcessor.refresh();
    }

    public List<Entry> refreshSelectedPacks(){
        List<Entry> selected = Lists.newArrayListWithCapacity(listPacksSelected.size());

        for(ResourcePackListEntry entry:listPacksSelected){
            if (!(entry instanceof ResourcePackListEntryFound))continue;

            ResourcePackListEntryFound packEntry = (ResourcePackListEntryFound)entry;

            if (packEntry.func_148318_i() != null){
                selected.add(packEntry.func_148318_i());
            }
        }

        Collections.reverse(selected);

        mc.getResourcePackRepository().setRepositories(selected);
        return selected;
    }

    private List<ResourcePackListEntryFound> createAvailablePackList(ResourcePackRepository repository){
        final List<ResourcePackListEntryFound> list = Lists.newArrayList();

        if (!repository.getDirResourcepacks().equals(currentFolder)){
            list.add(new ResourcePackListEntryFolder(this,currentFolder.getParentFile(),true));
        }

        final File[] files = currentFolder.listFiles();

        if (files != null){
            for(File file:files){
                if (file.isDirectory() && !new File(file,"pack.mcmeta").isFile()){
                    list.add(new ResourcePackListEntryFolder(this,file));
                }
                else{
                    Entry entry = ResourcePackRepositoryCustom.createEntryInstance(repository,file);

                    if (entry != null){
                        try{
                            entry.updateResourcePack();
                            list.add(new ResourcePackListEntryFound(this,entry));
                        } catch(Exception e) {}
                    }
                }
            }
        }

        List<Entry> repositoryEntries = repository.getRepositoryEntries();

        list.removeIf(listEntry -> listEntry.func_148318_i() != null && repositoryEntries.contains(listEntry.func_148318_i()));

        return list;
    }

    @Override
    public boolean hasResourcePackEntry(ResourcePackListEntry entry) {
        return listPacksSelected.contains(entry);
    }

    @Override
    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry entry) {
        return hasResourcePackEntry(entry) ? listPacksSelected : listPacksAvailable;
    }

    @Override
    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        hasUpdated = true;
        listPacksDummy.clear();
        return listPacksDummy;
    }

    @Override
    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        hasUpdated = true;
        return listPacksSelected;
    }

    @Override
    public void markChanged() {
        requiresReload = true;
    }

    public static void openFolder(File file) {
        String s = file.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.OSX) {
            try{
                Runtime.getRuntime().exec(new String[]{ "/usr/bin/open", s });
                return;
            } catch(Exception e){
            }
        }
        else if (Util.getOSType() == Util.EnumOS.WINDOWS){
            String command = String.format("cmd.exe /C start \"Open file\" \"%s\"",s);

            try{
                Runtime.getRuntime().exec(command);
                return;
            }catch(Exception e){}
        }

        try{
            final Class cls = Class.forName("java.awt.Desktop");
            final Object desktop = cls.getMethod("getDesktop").invoke(null);

            cls.getMethod("browse", URI.class).invoke(desktop,file.toURI());
        } catch(Throwable t) {
            Sys.openURL("file://"+s);
        }
    }

    public static void renderFolderEntry(ResourcePackListEntryFolder entry, int x, int y, boolean isSelected){
        Minecraft mc = Minecraft.getMinecraft();

        entry.func_148313_c();
        GlStateManager.color(1F,1F,1F,1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0F,0F,32,32,32F,32F);
        GlStateManager.disableBlend();

        int i2;

        if ((mc.gameSettings.touchscreen||isSelected)&&entry.func_148310_d()){
            Gui.drawRect(x,y,x+32,y+32,-1601138544);
            GlStateManager.color(1F,1F,1F,1F);
        }

        String s = entry.func_148312_b();
        i2 = mc.fontRendererObj.getStringWidth(s);

        if (i2>157){
            s = mc.fontRendererObj.trimStringToWidth(s,157-mc.fontRendererObj.getStringWidth("..."))+"...";
        }

        mc.fontRendererObj.drawStringWithShadow(s,x+32+2,y+1,16777215);
        List list = mc.fontRendererObj.listFormattedStringToWidth(entry.func_148311_a(),157);

        for(int j2 = 0; j2<2&&j2<list.size(); ++j2){
            mc.fontRendererObj.drawStringWithShadow((String)list.get(j2),x+32+2,y+12+10*j2,8421504);
        }
    }
}
