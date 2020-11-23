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

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

import java.lang.reflect.Field;

public class ReflectionCache {

    public Timer timer;

    public ReflectionCache() {
        getCache();
    }

    public void refreshCache() {
        getCache();
    }

    private void getCache() {
        try {
            timer = (Timer) getField("timer", "field_71428_T").get(Minecraft.getMinecraft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getField(String mcpName, String srgName) {
        Field f = null;
        try {
            f = Minecraft.class.getDeclaredField(mcpName);
        }
        catch (NoSuchFieldException e) {
            try {
                f = Minecraft.class.getDeclaredField(srgName);
            }
            catch (NoSuchFieldException e2) {
                e.printStackTrace();
            }
        }
        if (f != null) {
            f.setAccessible(true);
            return f;
        }
        return null;
    }

}
