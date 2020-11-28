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

package net.evergreen.client.mod;

import net.evergreen.client.exception.IllegalAnnotationException;
import net.evergreen.client.mod.impl.betterparticles.BetterParticles;
import net.evergreen.client.mod.impl.lowhptint.LowHpTint;
import net.evergreen.client.mod.impl.simplestats.SimpleStats;
import net.evergreen.client.setting.ConfigPosition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager {

    private final List<Mod> mods = new CopyOnWriteArrayList<>();

    public void registerMods() {
        addMod(new SimpleStats());
        addMod(new LowHpTint());
        addMod(new BetterParticles());
    }

    private void addMod(Mod mod) {
        mods.add(mod);
    }

    public void initialiseMods() {
        for (Mod mod : mods) {
            mod.initialise();
        }
    }

    public void loadModSettings() {
        for (Mod mod : mods) {
            mod.loadSettings();
        }
    }

    public Mod getMod(Class<? extends Mod> clazz) {
        return mods.stream().filter(m -> m.getClass().getName().equals(clazz.getName())).findAny().orElse(null);
    }

    public List<Mod> getMods() {
        return mods;
    }

    public List<Mod> getMods(ModMeta.Category category) {
        List<Mod> mods = new ArrayList<>();
        for (Mod m : getMods()) {
            if (m.getMetadata().getCategory() == category)
                mods.add(m);
        }
        return mods;
    }

    /**
     * Gets annotated field with specified type for positioning
     *
     * @param modInstance instance of the mod
     * @param type type of position
     * @return value of position
     */
    public float getPosition(Mod modInstance, ConfigPosition.Type type) {
        Float val = null;
        Class<?> clazz = modInstance.getClass();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        for (Field f : fields) {
            if (f.isAnnotationPresent(ConfigPosition.class)) {
                if (f.getAnnotation(ConfigPosition.class).type() != type) continue;
                try {
                    val = (Float) f.get(modInstance);
                }
                catch (ClassCastException e) {
                    throw new IllegalAnnotationException("Incorrect position object type. Should be float.");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return val;
    }

}
