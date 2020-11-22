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

package net.evergreen.client.command;

import net.evergreen.client.event.EventCommandSent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.*;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

/**
 * The class that handles client-side chat commands. You should register any
 * commands that you want handled on the client with this command handler.
 * <p>
 * If there is a command with the same name registered both on the server and
 * client, the client takes precedence!
 */
public class ClientCommandHandler extends CommandHandler {

    public static final ClientCommandHandler instance = new ClientCommandHandler();

    public String[] latestAutoComplete = null;

    /**
     * Attempt to execute a command. This method should return the number of times that the command was executed. If the
     * command does not exist or if the player does not have permission, 0 will be returned. A number greater than 1 can
     * be returned if a player selector is used.
     *
     * @param sender  The person who executed the command. This could be an EntityPlayer, RCon Source, Command Block,
     *                etc.
     * @param message The raw arguments that were passed. This includes the command name.
     * @return 1 if successfully executed, -1 if no permission or wrong usage,
     * 0 if it doesn't exist or it was canceled (it's sent to the server)
     */
    @Override
    public int executeCommand(ICommandSender sender, String message) {
        message = message.trim();

        if (message.startsWith("/")) {
            message = message.substring(1);
        }

        String[] temp = message.split(" ");
        String[] args = new String[temp.length - 1];
        String commandName = temp[0];
        System.arraycopy(temp, 1, args, 0, args.length);
        ICommand icommand = getCommands().get(commandName);

        try {
            if (icommand == null) {
                return 0;
            }

            if (icommand.canCommandSenderUseCommand(sender)) {
                EventCommandSent event = new EventCommandSent(icommand, sender, args);
                if (event.postCancellable()) {
                    if (event.exception != null) {
                        throw event.exception;
                    }
                    return 0;
                }

                icommand.processCommand(sender, args);
                return 1;
            } else {
                sender.addChatMessage(format(RED, "commands.generic.permission"));
            }
        } catch (WrongUsageException wue) {
            sender.addChatMessage(format(RED, "commands.generic.usage", format(RED, wue.getMessage(), wue.getErrorObjects())));
        } catch (CommandException ce) {
            sender.addChatMessage(format(RED, ce.getMessage(), ce.getErrorObjects()));
        } catch (Throwable t) {
            sender.addChatMessage(format(RED, "commands.generic.exception"));
            t.printStackTrace();
        }

        return -1;
    }

    // Couple of helpers because the mcp names are stupid and long...
    private ChatComponentTranslation format(EnumChatFormatting color, String str, Object... args) {
        ChatComponentTranslation ret = new ChatComponentTranslation(str, args);
        ret.getChatStyle().setColor(color);
        return ret;
    }

    public void autoComplete(String leftOfCursor) {
        latestAutoComplete = null;

        if (leftOfCursor.charAt(0) == '/') {
            leftOfCursor = leftOfCursor.substring(1);

            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen instanceof GuiChat) {
                List<String> commands = getTabCompletionOptions(mc.thePlayer, leftOfCursor, mc.thePlayer.getPosition());
                if (commands != null && !commands.isEmpty()) {
                    if (leftOfCursor.indexOf(' ') == -1) {
                        for (int i = 0; i < commands.size(); i++) {
                            commands.set(i, GRAY + "/" + commands.get(i) + RESET);
                        }
                    } else {
                        for (int i = 0; i < commands.size(); i++) {
                            commands.set(i, GRAY + commands.get(i) + RESET);
                        }
                    }

                    latestAutoComplete = commands.toArray(new String[commands.size()]);
                }
            }
        }
    }

}
