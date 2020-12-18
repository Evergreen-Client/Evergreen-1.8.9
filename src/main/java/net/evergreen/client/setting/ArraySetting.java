/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.setting;

import java.util.Arrays;
import java.util.List;

public class ArraySetting extends Setting {

    private int index;
    private final List<String> options;

    public ArraySetting(String displayName, String description, String defaultMode, String... modes) {
        super(displayName, description);
        this.options = Arrays.asList(modes);
        this.index = this.options.indexOf(defaultMode);
    }

    public String get() {
        return options.get(index);
    }

    public void set(String option) {
        index = options.indexOf(option);
    }

    public boolean is(String option) {
        return index == options.indexOf(option);
    }

    public void cycle() {
        if (index < options.size() - 1)
            index++;
        else
            index = 0;
    }
}
