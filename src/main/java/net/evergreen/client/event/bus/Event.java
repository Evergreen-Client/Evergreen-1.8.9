/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event.bus;

import net.evergreen.client.Evergreen;

public class Event {

    public boolean post() {
        Evergreen.EVENT_BUS.post(this);
        return false;
    }

}
