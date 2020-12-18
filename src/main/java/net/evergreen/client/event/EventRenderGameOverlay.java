/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.CancellableEvent;
import net.evergreen.client.event.bus.Event;

public class EventRenderGameOverlay {

    public static class Pre extends CancellableEvent {

        private final ElementType type;
        private final float partialTicks;

        public Pre(ElementType type, float partialTicks) {
            this.type = type;
            this.partialTicks = partialTicks;
        }

        public ElementType getType() {
            return type;
        }

        public float getPartialTicks() {
            return partialTicks;
        }
    }

    public static class Post extends Event {

        private final ElementType type;
        private final float partialTicks;

        public Post(ElementType type, float partialTicks) {
            this.type = type;
            this.partialTicks = partialTicks;
        }

        public ElementType getType() {
            return type;
        }

        public float getPartialTicks() {
            return partialTicks;
        }

    }

    public enum ElementType {
        FOOD,
        TEXT
    }

}
