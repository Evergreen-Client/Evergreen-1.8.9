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

package net.evergreen.client.mod;

import net.minecraft.util.ResourceLocation;

public class ModMeta {

    private final String name;
    private final String description;
    private final String author;
    private final Category category;
    private final ResourceLocation icon;

    public ModMeta(String name, String description, String author, Category category, ResourceLocation icon) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.category = category;
        this.icon = icon;
    }

    public ModMeta(String name, String description, Category category, ResourceLocation icon) {
        this(name, description, "Evergreen Team", category, icon);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public enum Category {
        GRAPHIC("Graphical"),
        QOL("Quality of Life"),
        OTHER("Other");

        public final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }
    }

}
