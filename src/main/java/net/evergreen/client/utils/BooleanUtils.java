/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.utils;

public class BooleanUtils {

    public static boolean allNull(Object... objects) {
        for (Object o : objects) {
            if (o != null)
                return false;
        }
        return true;
    }

    public static boolean noneNull(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return false;
        }
        return true;
    }

}
