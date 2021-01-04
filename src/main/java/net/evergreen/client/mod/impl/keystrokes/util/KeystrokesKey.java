/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mod.impl.keystrokes.util;

public class KeystrokesKey {

    public int key, width, height, x, y;

    public KeystrokesKey(int key, int width, int height, int x, int y) {
        this.key = key;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

}
