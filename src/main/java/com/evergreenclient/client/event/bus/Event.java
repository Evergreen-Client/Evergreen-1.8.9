/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.event.bus;

import com.evergreenclient.client.Evergreen;

public class Event {

    public boolean post() {
        Evergreen.EVENT_BUS.post(this);
        return false;
    }

}
