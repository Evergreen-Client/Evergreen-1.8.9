/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.event;

import com.evergreenclient.client.event.bus.CancellableEvent;
import com.evergreenclient.client.event.bus.Event;

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
