/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mixins.inject;

import net.evergreen.client.Evergreen;
import net.evergreen.client.discord.EvergreenRPC;
import net.evergreen.client.gui.mainmenu.GuiEvergreenMainMenu;
import net.evergreen.client.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    @Inject(method = "initGui", at = @At(value = "HEAD"))
    private void injectNewGuiScreen(CallbackInfo ci) {
        //Minecraft.getMinecraft().displayGuiScreen(new GuiEvergreenMainMenu());
    }

}
