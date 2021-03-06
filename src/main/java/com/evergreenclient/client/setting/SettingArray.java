/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.setting;

import java.util.Arrays;
import java.util.List;

public class SettingArray {

    private int index;
    private List<String> values;

    public SettingArray(String current, String... values) {
        this.values = Arrays.asList(values);
        this.index = this.values.indexOf(current);
    }

    public String getCurrent() {
        return this.values.get(index);
    }

    public void next() {
        if (index + 1 > values.size() - 1)
            index = 0;
        else
            index += 1;
    }

    public void previous() {
        if (index - 1 < 0)
            index = values.size() - 1;
        else
            index -= 1;
    }

    public List<String> values() {
        return values;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCurrent(String current) {
        this.index = values.indexOf(current);
    }

}
