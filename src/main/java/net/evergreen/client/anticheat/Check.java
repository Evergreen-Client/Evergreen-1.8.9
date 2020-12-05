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

package net.evergreen.client.anticheat;

import net.evergreen.client.Evergreen;
import net.evergreen.client.utils.ServerUtils;
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