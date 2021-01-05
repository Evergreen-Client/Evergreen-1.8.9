/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.event.EventEntityAttackEntity;
import com.evergreenclient.client.event.EventEntityJoinWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.*;
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
     * @reason event hook {@link EventEntityAttackEntity}
     */
    @Inject(method = "spawnEntityInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunkFromChunkCoords(II)Lnet/minecraft/world/chunk/Chunk;", shift = At.Shift.BEFORE), cancellable = true)
    private void injectEntityJoinWorld(Entity entityIn, CallbackInfoReturnable<Boolean> ci) {
        if (new EventEntityJoinWorld(entityIn, (World) (Object) this).post()) ci.setReturnValue(false);
    }

    /**
     * @author isXander
     * @reason event hook {@link EventEntityAttackEntity}
     */
    @Overwrite
    public void loadEntities(Collection<Entity> entityCollection) {
        for (Entity entity : entityCollection) {
            if (!(new EventEntityJoinWorld(entity, (World) (Object) this).post())) {
                loadedEntityList.add(entity);
                this.onEntityAdded(entity);
            }
        }
    }

    /**
     * @author isXander
     * @reason event hook {@link EventEntityAttackEntity}
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
            if (!(new EventEntityJoinWorld(entityIn, (World) (Object) this).post()))
                this.loadedEntityList.add(entityIn);
        }
    }

    /**
     * @return horizon level
     * @author isXander
     * @reason Void flicker fix
     */
    @Overwrite
    public double getHorizon() {
        return 0.0D;
    }

}
