/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.event;

import com.evergreenclient.client.event.bus.CancellableEvent;

public class EventCommandSent extends CancellableEvent {

    public String commandName;
    public String[] parameters;

    public EventCommandSent(String commandName, String[] args) {
        this.commandName = commandName;
        this.parameters = args;
    }

}
