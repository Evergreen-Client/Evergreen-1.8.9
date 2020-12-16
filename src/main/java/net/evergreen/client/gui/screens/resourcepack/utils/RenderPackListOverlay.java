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

import com.google.common.collect.Lists;
import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventRenderGameOverlay;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public final class RenderPackListOverlay {

    private static final RenderPackListOverlay instance = new RenderPackListOverlay();
    private static boolean isRegistered;

    public static void register() {
        if (isRegistered)return;

        isRegistered = true;
        Evergreen.EVENT_BUS.register(instance);
        refreshPackNames();
    }

    public static void unregister(){
        if (!isRegistered)return;

        isRegistered = false;
        Evergreen.EVENT_BUS.unregister(instance);
    }

    public static void refreshPackNames(){
        instance.refresh();
    }

    private List<String> packNames = new ArrayList<>(4);

    private void refresh(){
        packNames.clear();

        List<ResourcePackRepository.Entry> entries = Lists.reverse(Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries());

        for(ResourcePackRepository.Entry entry : entries){
            String name = entry.getResourcePackName();

            if (name.endsWith(".zip")){
                name = name.substring(0,name.length()-4);
            }

            packNames.add(name);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(EventRenderGameOverlay.Post e){
//        if (e.getType() == EventRenderGameOverlay.ElementType.TEXT && !packNames.isEmpty()){
//            DisplayPosition position = ResourcePackOrganizer.getConfig().options.getDisplayPosition();
//
//            if ((position == DisplayPosition.TOP_LEFT || position == DisplayPosition.TOP_RIGHT) && Minecraft.getMinecraft().gameSettings.showDebugInfo){
//                return;
//            }
//
//            final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
//            final int color = ResourcePackOrganizer.getConfig().options.getDisplayColor();
//
//            final int edgeDist = 3, topOffset = 2;
//            final int ySpacing = font.FONT_HEIGHT;
//
//            int x = position == DisplayPosition.TOP_LEFT || position == DisplayPosition.BOTTOM_LEFT ? edgeDist : e.resolution.getScaledWidth()-edgeDist;
//            int y = position == DisplayPosition.TOP_LEFT || position == DisplayPosition.TOP_RIGHT ? edgeDist : e.resolution.getScaledHeight()-edgeDist-topOffset-ySpacing*(1+packNames.size());
//            boolean alignRight = position == DisplayPosition.TOP_RIGHT || position == DisplayPosition.BOTTOM_RIGHT;
//
//            renderText(font, EnumChatFormatting.UNDERLINE+"Resource Packs",x,y,color,alignRight);
//
//            for(int line = 0; line < packNames.size(); line++){
//                renderText(font,packNames.get(line),x,y+topOffset+(line+1)*ySpacing,color,alignRight);
//            }
//        }
    }

    private static void renderText(FontRenderer renderer, String line, int x, int y, int color, boolean alignRight){
        renderer.drawString(line,alignRight ? x-renderer.getStringWidth(line) : x,y,color,color != 0);
    }

}
