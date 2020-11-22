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
