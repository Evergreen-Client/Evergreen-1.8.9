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
