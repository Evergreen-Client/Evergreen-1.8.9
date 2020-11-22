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

import cc.hyperium.event.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModManager {

    private List<Mod> mods = new CopyOnWriteArrayList<>();

    public void registerMods() {
        // Add mods here
        // addMod(new ExampleMod());
    }

    private void addMod(Mod mod) {
        mods.add(mod);
    }

    public void initialiseMods() {
        for (Mod mod : mods) {
            mod.initialise();
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

}
