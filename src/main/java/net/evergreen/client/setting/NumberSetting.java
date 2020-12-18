/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.setting;

public class NumberSetting extends Setting {

    private Number value;
    private Number min;
    private Number max;
    private final String prefix;
    private final String suffix;
    private final StoreType type;

    public NumberSetting(Number def, Number min, Number max, String displayName, String description, StoreType type, String prefix, String suffix) {
        super(displayName, description);
        this.value = def;
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
    }

    @Override
    public String getDisplayName() {
        return prefix + displayName + suffix;
    }

    public double getDouble() {
        return value.doubleValue();
    }

    public float getFloat() {
        return value.floatValue();
    }

    public int getInt() {
        return value.intValue();
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public StoreType getType() {
        return type;
    }

    public enum StoreType {
        INTEGER,
        FLOAT,
        DOUBLE
    }

}
