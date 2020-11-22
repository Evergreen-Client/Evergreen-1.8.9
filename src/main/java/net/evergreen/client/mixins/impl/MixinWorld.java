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

import net.evergreen.client.event.EventEntityJoinWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow @Final public List<Entity> loadedEntityList;

    @Shadow protected abstract void onEntityAdded(Entity entityIn);

    @Shadow public abstract Chunk getChunkFromChunkCoords(int chunkX, int chunkZ);

    /**
     * @author isXander
     * @reason event hook
     */
    @Inject(method = "spawnEntityInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunkFromChunkCoords(II)Lnet/minecraft/world/chunk/Chunk;", shift = At.Shift.BEFORE))
    private void injectEntityJoinWorld(Entity entityIn, CallbackInfoReturnable<Boolean> ci) {
        if (new EventEntityJoinWorld(entityIn, (World) (Object) this).postCancellable()) ci.setReturnValue(false);
    }

    /**
     * @author isXander
     * @reason event hook
     */
    @Overwrite
    public void loadEntities(Collection<Entity> entityCollection) {
        for (Entity entity : entityCollection) {
            if (!(new EventEntityJoinWorld(entity, (World) (Object) this).postCancellable())) {
                loadedEntityList.add(entity);
                this.onEntityAdded(entity);
            }
        }
    }

    /**
     * @author isXander
     * @reason event hook
     */
    @Overwrite
    public void joinEntityInSurroundings(Entity entityIn) {
        int i = MathHelper.floor_double(entityIn.posX / 16.0D);
        int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
        int k = 2;

        for (int l = i - k; l <= i + k; ++l) {
            for (int i1 = j - k; i1 <= j + k; ++i1) {
                this.getChunkFromChunkCoords(l, i1);
            }
        }

        if (!this.loadedEntityList.contains(entityIn)) {
            if (!(new EventEntityJoinWorld(entityIn, (World) (Object) this).postCancellable()))
                this.loadedEntityList.add(entityIn);
        }
    }

}
