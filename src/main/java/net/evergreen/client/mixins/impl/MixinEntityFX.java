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

package net.evergreen.client.mixins.impl;

import net.evergreen.client.Evergreen;
import net.evergreen.client.mod.impl.betterparticles.BetterParticles;
import net.evergreen.client.utils.MathUtils;
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
        BetterParticles bp = (BetterParticles) Evergreen.getInstance().getModManager().getMod(BetterParticles.class);
        setAlphaF(MathUtils.lerp(getAlpha(), 0, partialTicks * (bp.alphaSpeed.getFloat() / 100f)));
    }

}
