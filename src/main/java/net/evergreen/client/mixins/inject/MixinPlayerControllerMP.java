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

package net.evergreen.client.mixins.inject;

import net.evergreen.client.event.EventEntityAttackEntity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Shadow protected abstract void syncCurrentPlayItem();

    @Shadow @Final private NetHandlerPlayClient netClientHandler;

    @Shadow private WorldSettings.GameType currentGameType;

    /**
     * @author isXander
     * @reason Inject {@link EventEntityAttackEntity} event
     *
     * @param playerIn attacker
     * @param targetEntity victim
     */
    @Overwrite
    public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
        EventEntityAttackEntity event = new EventEntityAttackEntity(playerIn, targetEntity);

        if (!event.post()) {
            this.syncCurrentPlayItem();
            this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));

            if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
                playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }

    }

}
