/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.setting;

/**
 * Setting that contains boolean value
 * Displayed like a switch
 *
 * @author isXander
 */
public class BooleanSetting extends Setting {

    private boolean value;

    public BooleanSetting(boolean def, String displayName, String description) {
        super(displayName, description);
        this.value = def;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
