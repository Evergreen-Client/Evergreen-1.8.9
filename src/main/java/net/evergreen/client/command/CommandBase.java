/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.command;

import net.minecraft.command.CommandException;

import java.util.List;

public abstract class CommandBase {

    public abstract List<String> getAliases();

    public abstract void processCommand(List<String> args) throws CommandException;

    public String getCommandUsage() {
        return getAliases().get(0);
    }

    public static boolean doesStringStartWith(String original, String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

}
