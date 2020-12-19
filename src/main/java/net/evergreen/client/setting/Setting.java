/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.setting;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.awt.*;

/**
 * Setting class that can be used for GUIs and saving
 * Not to be used within an actual mod
 *
 * @author isXander
 */
public class Setting<T> {

    protected final String displayName;
    protected final String description;
    protected T value;

    protected int min, max;

    protected String prefix, suffix;

    protected PropertyType type;

    public Setting(T value, PropertyType type, String displayName, String description, int min, int max, String prefix, String suffix) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
        if (value instanceof Integer || value instanceof Float) {
            this.min = min;
            this.max = max;
        }
    }

    public Setting(T value, PropertyType type, String displayName, String description, String prefix, String suffix) {
        this(value, type, displayName, description, 0, 0, prefix, suffix);
    }

    public String getDisplayName() {
        return displayName;
    }

    public final String getJsonKeyName() {
        return displayName.trim().toLowerCase().replace(' ', '-');
    }

    public String getDescription() {
        return description;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    public PropertyType type() {
        return type;
    }

    public enum PropertyType {
        TEXT,
        FLOAT,
        INTEGER,
        COLOR,
        BOOLEAN,
        ARRAY
    }

    @Override
    public String toString() {
        return "Setting{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", min=" + min +
                ", max=" + max +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", type=" + type +
                '}';
    }
}
