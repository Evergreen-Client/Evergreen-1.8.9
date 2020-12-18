/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
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
