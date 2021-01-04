/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EventEntityAttackEntity extends CancellableEvent {

    public EntityPlayer attacker;
    public Entity victim;

    public EventEntityAttackEntity(EntityPlayer attacker, Entity victim) {
        this.attacker = attacker;
        this.victim = victim;
    }

}
