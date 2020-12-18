/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ServerUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void kickFromServer(IChatComponent reason) {
        if (mc.getCurrentServerData() != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);

            mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }

}
