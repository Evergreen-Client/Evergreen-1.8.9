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

package net.evergreen.client.utils;

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
