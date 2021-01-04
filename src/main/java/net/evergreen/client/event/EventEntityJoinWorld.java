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
import net.minecraft.world.World;

public class EventEntityJoinWorld extends CancellableEvent {

    private final Entity entity;
    private final World world;

    public EventEntityJoinWorld(Entity entityIn, World world) {
        this.entity = entityIn;
        this.world = world;
    }

    public Entity getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }
}
