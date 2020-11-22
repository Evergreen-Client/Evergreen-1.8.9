package net.evergreen.client.injection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class ClientRegistry {

    /**
     * Adds keybind into the game natively.
     *
     * @param key keybind that will be added
     */
    public static void registerKeyBind(KeyBinding key) {
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, key);
    }

}
