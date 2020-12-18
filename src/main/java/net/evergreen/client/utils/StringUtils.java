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

import net.minecraft.client.gui.FontRenderer;

public class StringUtils {

    public static String trimStringToWidth(String str, FontRenderer fr, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (fr.getStringWidth(sb.toString()) > width) {
            sb.replace(sb.length() - 5, sb.length(), "...");
        }
        return sb.toString();
    }

}
