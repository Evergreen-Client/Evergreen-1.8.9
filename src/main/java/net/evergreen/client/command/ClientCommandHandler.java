/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.command;

import net.evergreen.client.event.EventCommandSent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class ClientCommandHandler {

    public static final ClientCommandHandler instance = new ClientCommandHandler();

    private List<CommandBase> commands = new ArrayList<>();

    public String[] latestAutoComplete = null;

    public void registerCommand(CommandBase cmd) {
        commands.add(cmd);
    }

    public int executeCommand(String message) {
        message = message.trim();

        if (!message.startsWith("/"))
            return 0;

        message = message.substring(1);

        String[] tmp = message.split(" ");
        String[] args = new String[tmp.length - 1];
        String commandName = tmp[0];
        System.arraycopy(tmp, 1, args,  0, args.length);

        int count = 0;
        for (CommandBase cmd : commands) {
            if (cmd.getAliases().contains(commandName)) {
                EventCommandSent event = new EventCommandSent(commandName, args);
                if (!event.post()) {
                    try {
                        cmd.processCommand(Arrays.asList(event.parameters));
                        count++;
                    } catch (WrongUsageException wue) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(format(RED, "commands.generic.usage", format(RED, wue.getMessage(), wue.getErrorObjects())));
                    } catch (CommandException ce) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(format(RED, ce.getMessage(), ce.getErrorObjects()));
                    } catch (Throwable t) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(format(RED, "commands.generic.exception"));
                        t.printStackTrace();
                    }

                }
            }
        }
        if (count > 0)
            return 1;
        return 0;
    }

    private ChatComponentTranslation format(EnumChatFormatting color, String str, Object... args) {
        ChatComponentTranslation ret = new ChatComponentTranslation(str, args);
        ret.getChatStyle().setColor(color);
        return ret;
    }

    public void autoComplete(String leftOfCursor) {
        latestAutoComplete = null;

        if (leftOfCursor.charAt(0) != '/') return;

        leftOfCursor = leftOfCursor.substring(1);

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiChat) {
            List<String> commands = getTabCompletionOptions(leftOfCursor);
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

                latestAutoComplete = commands.toArray(new String[0]);
            }
        }
    }

    public List<String> getTabCompletionOptions(String input) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1) {
            List<String> list = new ArrayList<>();

            for (CommandBase command : this.commands) {
                if (CommandBase.doesStringStartWith(s, command.getAliases().get(0))) {
                    list.add(command.getAliases().get(0));
                }
            }

            return list;
        }
        else {
            return null;
        }
    }


}
