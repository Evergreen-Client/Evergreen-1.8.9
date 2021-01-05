/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

public class MathUtils {

    /**
     * Clamps value between 0 and 1 and returns value.
     *
     * @author isXander
     */
    public static float clamp01(float value) {
        if ((double)value < 0.0)
            return 0.0f;
        return (double)value > 1.0 ? 1f : value;
    }

    /**
     * Linearly interpolates between a and b by t.
     *
     * @param a Start value
     * @param b End value
     * @param t Interpolation between two floats
     * @return interpolated value between a - b
     * @author isXander
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * MathUtils.clamp01(t);
    }

    /**
     * Returns number between 0 - 1 depending on the range and value given
     *
     * @param val the value
     * @param min minimum of what the value can be
     * @param max maximum of what the value can be
     * @return converted percentage
     * @author isXander
     */
    public static float getPercent(float val, float min, float max) {
        return (val - min) / (max - min);
    }

}
