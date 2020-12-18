/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event.bus;

public enum Phase {
    PRE("Pre"),
    POST("Post");

    public String name;

    Phase(String name) {
        this.name = name;
    }
}
