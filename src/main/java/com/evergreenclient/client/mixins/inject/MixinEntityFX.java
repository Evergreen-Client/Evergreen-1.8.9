/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.mod.impl.betterparticles.BetterParticles;
import com.evergreenclient.client.utils.MathUtils;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFX.class)
public abstract class MixinEntityFX {

    @Shadow public abstract void setAlphaF(float alpha);

    @Shadow public abstract float getAlpha();

    @Inject(method = "renderParticle", at = @At("HEAD"))
    private void injectOpacity(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        BetterParticles bp = Evergreen.getInstance().getModManager().getMod(BetterParticles.class);
        setAlphaF(MathUtils.lerp(getAlpha(), 0, partialTicks * (bp.alphaSpeed / 100f)));
    }

}
