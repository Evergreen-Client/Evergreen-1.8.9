/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.anticheat;

import com.evergreenclient.client.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Check {

    protected final Logger logger = LogManager.getLogger("AntiCheat");
    protected final String name;
    protected final Minecraft mc = Minecraft.getMinecraft();

    protected Check(String name) {
        this.name = name;
    }

    protected void logSuspicion(String info) {
        logger.warn(String.format("(%s) %s", name, info));
        ServerUtils.kickFromServer(
                new ChatComponentText("Evergreen has detected you to be hacking. " +
                        "You have been kicked to prevent further damage to the server.")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

}
