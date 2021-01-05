/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.event.EventEntityAttackEntity;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.*;

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
