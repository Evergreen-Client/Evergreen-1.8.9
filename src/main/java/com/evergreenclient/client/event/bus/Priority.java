/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.event.bus;

public enum Priority {

    HIGH(-1), // Called first
    NORMAL(0),
    LOW(1); // Called last

    public final int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
