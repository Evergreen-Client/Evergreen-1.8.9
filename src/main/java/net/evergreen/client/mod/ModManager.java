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

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventClientShutdown;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.exception.IllegalAnnotationException;
import net.evergreen.client.setting.ConfigPosition;
import net.evergreen.client.utils.JarLoader;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager {

    private final List<Mod> mods = new CopyOnWriteArrayList<>();

    public ModManager() {
        Evergreen.EVENT_BUS.register(this);
    }

    /**
     * Looks through all classes in mod package for objects
     * that extend {@link Mod} and register them
     *
     * @author isXander
     */
    public void registerMods() {
        // Easier than registering each mod manually
        Reflections r1 = new Reflections("net.evergreen.client.mod.impl");
        for (Class<? extends Mod> m : r1.getSubTypesOf(Mod.class)) {
            try {
                addMod(m.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Repeat for external mods...
        Reflections r2 = new Reflections("evergreen");
        try {
            for (Class<? extends Mod> m : r2.getSubTypesOf(Mod.class)) {
                try {
                    addMod(m.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (ReflectionsException e) {
        }
    }

    private void addMod(Mod mod) {
        if (!mods.contains(mod))
            mods.add(mod);
    }

    public void initialiseMods() {
        for (Mod mod : mods) {
            mod.initialise();
            if (mod.backendMod())
                mod.setEnabled(true);
        }
    }

    public void loadModSettings() {
        for (Mod mod : mods) {
            mod.loadSettings();
        }
    }

    /**
     * Gets mod from registered list using class
     *
     * @param clazz class of mod
     * @return instance of mod that has been registered
     * @author isXander
     */
    public <T extends Mod> T getMod(Class<? extends T> clazz) {
        return (T)mods.stream().filter(m -> m.getClass().getName().equals(clazz.getName())).findAny().orElse(null);
    }

    public List<Mod> getMods() {
        return mods;
    }

    /**
     * Gets all mods with certain category
     *
     * @param category category to filter
     * @return list of mods with certain category
     * @author isXander
     */
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
     * @author isXander
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

    /**
     * Injects jar files in mod folder into classpath
     * so {@link ModManager} can find and register mod
     * classes to load.
     *
     * @author isXander
     */
    public void injectExternalMods() {
        File modFolder = new File(Evergreen.dataDir, "mods");
        if (modFolder.exists()) {
            for (File f : modFolder.listFiles()) {
                if (f.isFile()) {
                    if (FilenameUtils.getExtension(f.getName()).equalsIgnoreCase("jar")) {
                        try {
                            JarLoader.addToClasspath(f);
                        }
                        catch (UnsupportedClassVersionError e) {
                            throw new ReportedException(CrashReport.makeCrashReport(e, "Could not load mod " + f.getName()
                                    + "\nThis is because the mod was built with a newer JDK"
                                    + "\nTo fix this, make sure you are building your project with Java 8"));
                        }
                    }
                }
            }
        } else {
            modFolder.mkdirs();
        }
    }

    @SubscribeEvent
    public void onShutdown(EventClientShutdown event) {
        for (Mod mod : mods) {
            mod.saveSettings();
        }
    }

}
