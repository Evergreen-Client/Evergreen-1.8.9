/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventRenderGameOverlay;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderPlayerStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER), cancellable = true)
    private void injectPreRenderFood(ScaledResolution res, CallbackInfo ci) {
        if (new EventRenderGameOverlay.Pre(EventRenderGameOverlay.ElementType.FOOD, Evergreen.getInstance().getReflectionCache().timer.renderPartialTicks).post()) ci.cancel();
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderPlayerStats(Lnet/minecraft/client/gui/ScaledResolution;)V", shift = At.Shift.AFTER))
    private void injectPostRenderText(float partialTicks, CallbackInfo ci) {
        new EventRenderGameOverlay.Post(EventRenderGameOverlay.ElementType.TEXT, partialTicks);
    }

}
