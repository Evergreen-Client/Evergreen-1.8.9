/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

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
