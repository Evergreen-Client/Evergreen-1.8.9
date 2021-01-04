/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.event.bus;

import java.lang.reflect.Method;

public class EventMethod {

    private final Object instance;
    private final Method method;
    private final Class<?> event;
    private final SubscribeEvent annotation;

    public EventMethod(Object instance, Method method, Class<?> event, SubscribeEvent annotation) {
        this.instance = instance;
        this.method = method;
        this.event = event;
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEvent() {
        return event;
    }

    public Object getInstance() {
        return instance;
    }

    public SubscribeEvent getAnnotation() {
        return annotation;
    }

    @Override
    public String toString() {
        return "EventMethod{" +
                "instance=" + instance +
                ", method=" + method +
                ", event=" + event +
                ", annotation=" + annotation +
                '}';
    }
}
